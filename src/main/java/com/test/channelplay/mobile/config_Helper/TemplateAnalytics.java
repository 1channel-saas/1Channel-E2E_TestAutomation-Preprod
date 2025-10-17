package com.test.channelplay.mobile.config_Helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Template Analytics and Reporting
 *
 * Provides insights into template health, performance, and recommendations
 */
public class TemplateAnalytics {

    private static final Logger log = LoggerFactory.getLogger(TemplateAnalytics.class);
    private static final String TEMPLATES_BASE = "templates";

    // ========== Query Methods ==========

    /**
     * Get all templates with enhanced metadata
     */
    public static List<TemplateMetadata> getAllTemplates() {
        List<TemplateMetadata> templates = new ArrayList<>();

        try {
            Files.walk(Paths.get(TEMPLATES_BASE))
                .filter(path -> path.toString().endsWith(".json"))
                .filter(path -> !path.toString().contains("visual_index"))
                .forEach(path -> {
                    try {
                        TemplateMetadata metadata =
                            TemplateMetadata.loadOrCreate(
                                path.toString().replace(".json", ".png")
                            );
                        templates.add(metadata);
                    } catch (Exception e) {
                        log.debug("Skipping non-enhanced metadata: {}", path);
                    }
                });
        } catch (IOException e) {
            log.error("Failed to scan templates: {}", e.getMessage());
        }

        return templates;
    }

    /**
     * Get templates that need attention
     */
    public static List<TemplateMetadata> getTemplatesNeedingAttention() {
        return getAllTemplates().stream()
            .filter(TemplateMetadata::needsAttention)
            .sorted(Comparator.comparing(t -> t.health.status))
            .collect(Collectors.toList());
    }

    /**
     * Get templates by health status
     */
    public static List<TemplateMetadata> getTemplatesByHealth(String status) {
        return getAllTemplates().stream()
            .filter(t -> t.health.status.equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }

    /**
     * Get top performing templates
     */
    public static List<TemplateMetadata> getTopPerformers(int limit) {
        return getAllTemplates().stream()
            .filter(t -> t.usage.totalAttempts > 3) // Only templates with enough data
            .sorted(Comparator.comparingDouble((TemplateMetadata t) -> t.usage.successRate).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    /**
     * Get worst performing templates
     */
    public static List<TemplateMetadata> getWorstPerformers(int limit) {
        return getAllTemplates().stream()
            .filter(t -> t.usage.totalAttempts > 3)
            .sorted(Comparator.comparingDouble(t -> t.usage.successRate))
            .limit(limit)
            .collect(Collectors.toList());
    }

    /**
     * Get unused templates (not used in N days)
     */
    public static List<TemplateMetadata> getUnusedTemplates(int days) {
        long cutoffTime = System.currentTimeMillis() - (days * 24L * 60 * 60 * 1000);

        return getAllTemplates().stream()
            .filter(t -> t.usage.lastUsed < cutoffTime)
            .sorted(Comparator.comparingLong(t -> t.usage.lastUsed))
            .collect(Collectors.toList());
    }

    /**
     * Search templates by tag
     */
    public static List<TemplateMetadata> getTemplatesByTag(String tag) {
        return getAllTemplates().stream()
            .filter(t -> t.tags.contains(tag))
            .collect(Collectors.toList());
    }

    /**
     * Search templates by screen name
     */
    public static List<TemplateMetadata> getTemplatesByScreen(String screenName) {
        return getAllTemplates().stream()
            .filter(t -> t.context.screenName.equalsIgnoreCase(screenName))
            .collect(Collectors.toList());
    }

    // ========== Analytics Methods ==========

    /**
     * Calculate global statistics
     */
    public static GlobalStats calculateGlobalStats() {
        List<TemplateMetadata> allTemplates = getAllTemplates();
        GlobalStats stats = new GlobalStats();

        stats.totalTemplates = allTemplates.size();

        // Count by type
        stats.manualCount = (int) allTemplates.stream()
            .filter(t -> t.context.elementType.equalsIgnoreCase("Manual"))
            .count();
        stats.autoCount = (int) allTemplates.stream()
            .filter(t -> t.context.elementType.equalsIgnoreCase("Auto"))
            .count();
        stats.aiCount = (int) allTemplates.stream()
            .filter(t -> t.context.elementType.equalsIgnoreCase("AI"))
            .count();

        // Count by health
        stats.healthyCount = (int) allTemplates.stream()
            .filter(t -> t.health.status.equalsIgnoreCase("healthy"))
            .count();
        stats.warningCount = (int) allTemplates.stream()
            .filter(t -> t.health.status.equalsIgnoreCase("warning"))
            .count();
        stats.criticalCount = (int) allTemplates.stream()
            .filter(t -> t.health.status.equalsIgnoreCase("critical"))
            .count();

        // Calculate average success rate
        stats.avgSuccessRate = allTemplates.stream()
            .filter(t -> t.usage.totalAttempts > 0)
            .mapToDouble(t -> t.usage.successRate)
            .average()
            .orElse(0.0);

        // Total usage
        stats.totalUsage = allTemplates.stream()
            .mapToInt(t -> t.usage.totalAttempts)
            .sum();

        // Average confidence
        stats.avgConfidence = allTemplates.stream()
            .filter(t -> t.performance.opencvAttempts > 0)
            .mapToDouble(t -> t.performance.avgConfidence)
            .average()
            .orElse(0.0);

        return stats;
    }

    public static class GlobalStats {
        public int totalTemplates;
        public int manualCount;
        public int autoCount;
        public int aiCount;
        public int healthyCount;
        public int warningCount;
        public int criticalCount;
        public double avgSuccessRate;
        public int totalUsage;
        public double avgConfidence;

        @Override
        public String toString() {
            return String.format(
                "=== Global Template Statistics ===\n" +
                "Total Templates: %d\n" +
                "  Manual: %d | Auto: %d | AI: %d\n" +
                "Health Status:\n" +
                "  Healthy: %d | Warning: %d | Critical: %d\n" +
                "Performance:\n" +
                "  Avg Success Rate: %.1f%%\n" +
                "  Avg Confidence: %.2f\n" +
                "  Total Usage: %d attempts\n",
                totalTemplates,
                manualCount, autoCount, aiCount,
                healthyCount, warningCount, criticalCount,
                avgSuccessRate * 100,
                avgConfidence,
                totalUsage
            );
        }
    }

    // ========== Reporting Methods ==========

    /**
     * Generate detailed report
     */
    public static String generateReport() {
        StringBuilder report = new StringBuilder();

        report.append("\n");
        report.append("╔════════════════════════════════════════════════════════════════╗\n");
        report.append("║          TEMPLATE HEALTH & PERFORMANCE REPORT                 ║\n");
        report.append("╚════════════════════════════════════════════════════════════════╝\n\n");

        // Global stats
        GlobalStats stats = calculateGlobalStats();
        report.append(stats.toString()).append("\n");

        // Templates needing attention
        List<TemplateMetadata> needsAttention = getTemplatesNeedingAttention();
        if (!needsAttention.isEmpty()) {
            report.append("=== TEMPLATES NEEDING ATTENTION (").append(needsAttention.size()).append(") ===\n");
            for (TemplateMetadata template : needsAttention) {
                report.append(String.format(
                    "  [%s] %s\n    Success: %.1f%% | Warnings: %s\n",
                    template.health.status.toUpperCase(),
                    template.elementName,
                    template.usage.successRate * 100,
                    String.join(", ", template.health.warnings)
                ));
            }
            report.append("\n");
        }

        // Top performers
        List<TemplateMetadata> topPerformers = getTopPerformers(5);
        if (!topPerformers.isEmpty()) {
            report.append("=== TOP 5 PERFORMERS ===\n");
            for (int i = 0; i < topPerformers.size(); i++) {
                TemplateMetadata template = topPerformers.get(i);
                report.append(String.format(
                    "  %d. %s - %.1f%% (%d attempts)\n",
                    i + 1,
                    template.elementName,
                    template.usage.successRate * 100,
                    template.usage.totalAttempts
                ));
            }
            report.append("\n");
        }

        // Unused templates
        List<TemplateMetadata> unused = getUnusedTemplates(30);
        if (!unused.isEmpty()) {
            report.append("=== UNUSED TEMPLATES (30+ days) ===\n");
            for (TemplateMetadata template : unused) {
                long daysSinceUsed = (System.currentTimeMillis() - template.usage.lastUsed) / (1000 * 60 * 60 * 24);
                report.append(String.format(
                    "  %s - Last used: %d days ago\n",
                    template.elementName,
                    daysSinceUsed
                ));
            }
            report.append("\n");
        }

        report.append("═══════════════════════════════════════════════════════════════\n");

        return report.toString();
    }

    /**
     * Print report to console
     */
    public static void printReport() {
        System.out.println(generateReport());
    }

    /**
     * Save report to file
     */
    public static void saveReport(String filename) {
        try {
            Files.write(Paths.get(filename), generateReport().getBytes());
            log.info("Report saved to: {}", filename);
        } catch (IOException e) {
            log.error("Failed to save report: {}", e.getMessage());
        }
    }

    // ========== Recommendation Engine ==========

    /**
     * Generate recommendations
     */
    public static List<Recommendation> generateRecommendations() {
        List<Recommendation> recommendations = new ArrayList<>();

        // Check for templates needing recapture
        getTemplatesByHealth("critical").forEach(template -> {
            recommendations.add(new Recommendation(
                "high",
                "recapture",
                template.elementName,
                String.format("Success rate is critical: %.1f%%", template.usage.successRate * 100),
                "Recapture template immediately - UI may have changed"
            ));
        });

        getTemplatesByHealth("warning").forEach(template -> {
            recommendations.add(new Recommendation(
                "medium",
                "recapture",
                template.elementName,
                String.format("Success rate below 70%%: %.1f%%", template.usage.successRate * 100),
                "Consider recapturing template"
            ));
        });

        // Check for unused templates
        getUnusedTemplates(30).forEach(template -> {
            long days = (System.currentTimeMillis() - template.usage.lastUsed) / (1000 * 60 * 60 * 24);
            recommendations.add(new Recommendation(
                "low",
                "cleanup",
                template.elementName,
                String.format("Not used in %d days", days),
                "Consider removing if no longer needed"
            ));
        });

        // Sort by priority
        recommendations.sort(Comparator.comparing(r -> r.priority.equals("high") ? 0 : r.priority.equals("medium") ? 1 : 2));

        return recommendations;
    }

    public static class Recommendation {
        public String priority; // high, medium, low
        public String type; // recapture, cleanup, optimize
        public String template;
        public String reason;
        public String suggestedAction;

        public Recommendation(String priority, String type, String template, String reason, String suggestedAction) {
            this.priority = priority;
            this.type = type;
            this.template = template;
            this.reason = reason;
            this.suggestedAction = suggestedAction;
        }

        @Override
        public String toString() {
            return String.format(
                "[%s] %s\n  Template: %s\n  Reason: %s\n  Action: %s",
                priority.toUpperCase(),
                type,
                template,
                reason,
                suggestedAction
            );
        }
    }
}