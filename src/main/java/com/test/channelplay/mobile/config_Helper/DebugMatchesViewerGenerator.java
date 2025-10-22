package com.test.channelplay.mobile.config_Helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Generates an HTML viewer for debug match images
 *
 * Scans screenshots/debug_matches folder and creates a dark-themed
 * HTML viewer with filtering, stats, and fullscreen image viewing.
 *
 * Usage:
 *   DebugMatchesViewerGenerator.generate("screenshots/debug_matches");
 */
public class DebugMatchesViewerGenerator {

    private static final Logger log = LoggerFactory.getLogger(DebugMatchesViewerGenerator.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Match data model
     */
    public static class MatchData {
        public String filename;
        public String path;
        public String method;        // "opencv" or "ocr"
        public String status;        // "success" or "failure"
        public Double confidence;    // OpenCV only, null for OCR
        public String strategy;
        public String element;
        public String timestamp;
        public String scenario;

        public MatchData(String filename, String path, String method, String status,
                        Double confidence, String strategy, String element,
                        String timestamp, String scenario) {
            this.filename = filename;
            this.path = path;
            this.method = method;
            this.status = status;
            this.confidence = confidence;
            this.strategy = strategy;
            this.element = element;
            this.timestamp = timestamp;
            this.scenario = scenario;
        }
    }

    /**
     * Parse filename to extract match metadata
     */
    private static MatchData parseFilename(String filename, String relativePath, String scenarioName) {
        // Extract method
        String method = filename.startsWith("opencv_") ? "opencv" : "ocr";

        // Extract status
        String status = filename.contains("_fail_") ? "failure" : "success";

        // Extract confidence (OpenCV only)
        Double confidence = null;
        if (method.equals("opencv")) {
            Pattern confPattern = Pattern.compile("_(match|fail)_(\\d+\\.\\d+)_");
            Matcher matcher = confPattern.matcher(filename);
            if (matcher.find()) {
                try {
                    confidence = Double.parseDouble(matcher.group(2));
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse confidence from: {}", filename);
                }
            }
        }

        // Extract strategy
        String strategy = "Unknown";
        if (method.equals("ocr")) {
            if (filename.contains("Two-pass_OCR") || filename.contains("Two_pass_OCR")) {
                strategy = "Two-pass OCR";
            } else if (filename.contains("Color-based") || filename.contains("Color_based")) {
                strategy = "Color-based";
            } else if (filename.contains("Smart_offset") || filename.contains("Smart_Offset")) {
                strategy = "Smart Offset";
            }
        } else {
            strategy = "TM_CCOEFF_NORMED"; // Default, can be enhanced
        }

        // Extract element name
        String cleanName = filename.replace("opencv_", "").replace("ocr_", "");
        String[] parts = cleanName.split("_");
        StringBuilder element = new StringBuilder();
        for (String part : parts) {
            if (part.equals("match") || part.equals("fail") || part.matches("\\d+.*")) {
                break;
            }
            if (!part.matches("Two|pass|OCR|Color|based|Smart|offset|TM|CCOEFF|NORMED")) {
                if (element.length() > 0) element.append("_");
                element.append(part);
            }
        }

        // Extract timestamp
        Pattern tsPattern = Pattern.compile("_(\\d{13})\\.png$");
        Matcher tsMatcher = tsPattern.matcher(filename);
        String timestamp = "00:00:00.000";
        if (tsMatcher.find()) {
            try {
                long timestampMs = Long.parseLong(tsMatcher.group(1));
                LocalDateTime dt = LocalDateTime.ofEpochSecond(timestampMs / 1000,
                                                               (int)((timestampMs % 1000) * 1_000_000),
                                                               java.time.ZoneOffset.UTC);
                timestamp = dt.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
            } catch (Exception e) {
                log.warn("Failed to parse timestamp from: {}", filename);
            }
        }

        String elementName = element.length() > 0 ? element.toString() : "unknown";

        return new MatchData(filename, relativePath, method, status, confidence,
                           strategy, elementName, timestamp, scenarioName);
    }

    /**
     * Scan debug matches folder and collect all match data
     */
    private static List<MatchData> scanDebugFolder(String debugFolderPath) throws IOException {
        List<MatchData> matchData = new ArrayList<>();
        Path debugPath = Paths.get(debugFolderPath);

        if (!Files.exists(debugPath)) {
            log.warn("Debug folder not found: {}", debugFolderPath);
            return matchData;
        }

        // Scan all scenario folders
        Files.list(debugPath)
            .filter(Files::isDirectory)
            .forEach(scenarioFolder -> {
                String scenarioName = scenarioFolder.getFileName().toString();

                // Scan success and failures folders
                for (String subfolder : Arrays.asList("success", "failures")) {
                    Path subfolderPath = scenarioFolder.resolve(subfolder);
                    if (!Files.exists(subfolderPath)) continue;

                    try {
                        Files.list(subfolderPath)
                            .filter(p -> p.toString().toLowerCase().endsWith(".png"))
                            .forEach(imgFile -> {
                                String filename = imgFile.getFileName().toString();
                                String relativePath = scenarioName + "/" + subfolder + "/" + filename;
                                MatchData match = parseFilename(filename, relativePath, scenarioName);
                                matchData.add(match);
                            });
                    } catch (IOException e) {
                        log.error("Error scanning subfolder: {}", subfolderPath, e);
                    }
                }
            });

        return matchData;
    }

    /**
     * Generate HTML viewer with timestamped JSON data files
     */
    public static void generate(String debugFolderPath) {
        try {
            log.info("Generating debug matches viewer for: {}", debugFolderPath);

            // Scan folder
            List<MatchData> matchData = scanDebugFolder(debugFolderPath);

            if (matchData.isEmpty()) {
                log.warn("No debug images found in: {}", debugFolderPath);
                // Still generate HTML even if no current images (will load historical data)
            } else {
                log.info("Found {} debug images", matchData.size());

                // Calculate stats
                long opencvCount = matchData.stream().filter(m -> "opencv".equals(m.method)).count();
                long ocrCount = matchData.stream().filter(m -> "ocr".equals(m.method)).count();
                long successCount = matchData.stream().filter(m -> "success".equals(m.status)).count();

                log.info("  OpenCV: {} | OCR: {}", opencvCount, ocrCount);
                log.info("  Success: {} | Failures: {}", successCount, matchData.size() - successCount);

                // Save match data to timestamped JSON file (persists for 7 days)
                saveMatchDataJson(matchData);
            }

            // Clean up old JSON files (older than 7 days)
            cleanupOldJsonFiles();

            // Generate HTML viewer (loads data from all JSON files)
            String html = generateHtml();

            // Save HTML file
            Path outputPath = Paths.get(debugFolderPath, "debug_matches_viewer.html");
            Files.write(outputPath, html.getBytes("UTF-8"));

            log.info("✅ Debug viewer generated: {}", outputPath.toAbsolutePath());

        } catch (Exception e) {
            log.error("Failed to generate debug viewer", e);
        }
    }

    /**
     * Save match data to timestamped JSON file and update manifest
     */
    private static void saveMatchDataJson(List<MatchData> matchData) {
        try {
            // Create metadata folder (excluded from maven clean)
            String metadataFolder = "screenshots/debug_matches_metadata";
            Path metadataPath = Paths.get(metadataFolder);
            if (!Files.exists(metadataPath)) {
                Files.createDirectories(metadataPath);
            }

            // Generate timestamped filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String jsonFilename = String.format("debug_matches_%s.json", timestamp);
            Path jsonPath = metadataPath.resolve(jsonFilename);

            // Save JSON
            String json = gson.toJson(matchData);
            Files.write(jsonPath, json.getBytes("UTF-8"));

            log.info("Match data saved to: {}", jsonPath.toAbsolutePath());

            // Update manifest file
            updateManifest(metadataPath);

        } catch (Exception e) {
            log.warn("Failed to save match data JSON: {}", e.getMessage());
        }
    }

    /**
     * Update manifest file with list of all JSON files
     */
    private static void updateManifest(Path metadataPath) {
        try {
            // Get all JSON files (except manifest itself)
            List<String> jsonFiles = Files.list(metadataPath)
                .filter(p -> p.toString().endsWith(".json"))
                .filter(p -> !p.getFileName().toString().equals("manifest.json"))
                .map(p -> p.getFileName().toString())
                .sorted()
                .collect(Collectors.toList());

            // Create manifest with file list and metadata
            Map<String, Object> manifest = new HashMap<>();
            manifest.put("files", jsonFiles);
            manifest.put("count", jsonFiles.size());
            manifest.put("lastUpdated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // Save manifest
            Path manifestPath = metadataPath.resolve("manifest.json");
            String manifestJson = gson.toJson(manifest);
            Files.write(manifestPath, manifestJson.getBytes("UTF-8"));

            log.debug("Manifest updated with {} files", jsonFiles.size());

        } catch (Exception e) {
            log.warn("Failed to update manifest: {}", e.getMessage());
        }
    }

    /**
     * Clean up JSON files older than configured days (default 7)
     */
    private static void cleanupOldJsonFiles() {
        try {
            String metadataFolder = "screenshots/debug_matches_metadata";
            Path metadataPath = Paths.get(metadataFolder);

            if (!Files.exists(metadataPath)) {
                return;
            }

            // Get cleanup age from config (use preprod prefix)
            int maxAgeDays = 7; // Default
            try {
                String configValue = com.test.channelplay.utils.GetProperty.value("template.debug.metadata.retention.days");
                if (configValue != null && !configValue.isEmpty()) {
                    maxAgeDays = Integer.parseInt(configValue);
                }
            } catch (Exception e) {
                log.debug("Using default retention period: {} days", maxAgeDays);
            }

            long cutoffTime = System.currentTimeMillis() - (maxAgeDays * 24L * 60 * 60 * 1000);

            // Delete old JSON files (but not manifest)
            boolean filesDeleted = false;
            List<Path> filesToDelete = Files.list(metadataPath)
                .filter(p -> p.toString().endsWith(".json"))
                .filter(p -> !p.getFileName().toString().equals("manifest.json"))
                .filter(p -> {
                    try {
                        return Files.getLastModifiedTime(p).toMillis() < cutoffTime;
                    } catch (IOException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

            for (Path p : filesToDelete) {
                try {
                    Files.delete(p);
                    log.info("Deleted old metadata file: {}", p.getFileName());
                    filesDeleted = true;
                } catch (IOException e) {
                    log.warn("Failed to delete old metadata file: {}", p.getFileName());
                }
            }

            // Update manifest if any files were deleted
            if (filesDeleted) {
                updateManifest(metadataPath);
            }

        } catch (Exception e) {
            log.warn("Failed to cleanup old JSON files: {}", e.getMessage());
        }
    }

    /**
     * Generate HTML content with embedded JSON data from all metadata files
     */
    private static String generateHtml() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Load all match data from JSON files in metadata folder, grouped by test run
        List<Map<String, Object>> testRunGroups = new ArrayList<>();
        String metadataFolder = "screenshots/debug_matches_metadata";
        Path metadataPath = Paths.get(metadataFolder);
        String debugFolder = "screenshots/debug_matches";
        int totalMatches = 0;
        long sevenDaysAgoMillis = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);

        try {
            if (Files.exists(metadataPath)) {
                // Load manifest to get file list
                Path manifestPath = metadataPath.resolve("manifest.json");
                if (Files.exists(manifestPath)) {
                    String manifestContent = new String(Files.readAllBytes(manifestPath), "UTF-8");
                    Map<String, Object> manifest = gson.fromJson(manifestContent, Map.class);
                    List<String> files = (List<String>) manifest.get("files");

                    if (files != null) {
                        // Sort files in reverse chronological order (latest first)
                        List<String> sortedFiles = new ArrayList<>(files);
                        sortedFiles.sort(Collections.reverseOrder());

                        for (String filename : sortedFiles) {
                            try {
                                Path jsonPath = metadataPath.resolve(filename);
                                if (Files.exists(jsonPath)) {
                                    String jsonContent = new String(Files.readAllBytes(jsonPath), "UTF-8");
                                    MatchData[] data = gson.fromJson(jsonContent, MatchData[].class);

                                    // Filter out matches where image was deleted more than 7 days ago
                                    List<MatchData> validMatches = new ArrayList<>();
                                    for (MatchData match : data) {
                                        Path imagePath = Paths.get(debugFolder, match.path);
                                        boolean imageExists = Files.exists(imagePath);

                                        // Include match if:
                                        // 1. Image exists, OR
                                        // 2. Image doesn't exist but was deleted within last 7 days
                                        if (imageExists) {
                                            validMatches.add(match);
                                        } else {
                                            // Check if JSON file is newer than 7 days
                                            long jsonFileAge = Files.getLastModifiedTime(jsonPath).toMillis();
                                            if (jsonFileAge >= sevenDaysAgoMillis) {
                                                validMatches.add(match);
                                            }
                                        }
                                    }

                                    if (!validMatches.isEmpty()) {
                                        // Extract timestamp from filename (format: debug_matches_2025-10-18_14-38-26.json)
                                        String testRunTimestamp = filename.replace("debug_matches_", "").replace(".json", "");

                                        Map<String, Object> testRun = new HashMap<>();
                                        testRun.put("timestamp", testRunTimestamp);
                                        testRun.put("matches", validMatches);
                                        testRun.put("count", validMatches.size());
                                        testRunGroups.add(testRun);
                                        totalMatches += validMatches.size();

                                        log.debug("Loaded {} matches from {} (filtered from {} total)",
                                            validMatches.size(), filename, data.length);
                                    }
                                }
                            } catch (Exception e) {
                                log.warn("Failed to load {}: {}", filename, e.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load historical match data: {}", e.getMessage());
        }

        // Convert grouped data to JSON for embedding
        String testRunGroupsJson = gson.toJson(testRunGroups);

        String dataInfo = testRunGroups.isEmpty() ?
            "No historical data" :
            String.format("%d test runs, %d matches from last 7 days", testRunGroups.size(), totalMatches);

        // Generate HTML with embedded data
        return String.format(HTML_TEMPLATE_EMBEDDED, timestamp, dataInfo, testRunGroupsJson);
    }

    /**
     * Legacy method for backward compatibility (deprecated)
     */
    @Deprecated
    private static String generateHtml(List<MatchData> matchData, String scenarios) {
        String matchDataJson = gson.toJson(matchData);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Embedded HTML template (simplified for size - contains same styling as Python version)
        return String.format(HTML_TEMPLATE,
            scenarios,                    // {0}
            matchData.size(),            // {1}
            timestamp,                   // {2}
            matchDataJson                // {3}
        );
    }

    // HTML template (same as Python version but with placeholders)
    private static final String HTML_TEMPLATE =
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "  <meta charset=\"UTF-8\">\n" +
        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
        "  <title>\uD83D\uDD0D Debug Matches Viewer</title>\n" +
        "  <style>\n" +
        "    * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
        "    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #1a1a2e 0%%, #16213e 100%%); color: #e0e0e0; padding: 20px; min-height: 100vh; }\n" +
        "    .container { max-width: 1400px; margin: 0 auto; }\n" +
        "    header { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px 30px; margin-bottom: 30px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4); }\n" +
        "    header h1 { font-size: 32px; margin-bottom: 10px; background: linear-gradient(135deg, #00ff88, #00d4ff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }\n" +
        "    .scenario-info { display: flex; gap: 20px; color: #a0a0a0; font-size: 14px; flex-wrap: wrap; }\n" +
        "    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 30px; }\n" +
        "    .stat-card { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3); }\n" +
        "    .stat-card h3 { font-size: 14px; color: #a0a0a0; margin-bottom: 10px; text-transform: uppercase; letter-spacing: 1px; }\n" +
        "    .stat-card .value { font-size: 36px; font-weight: bold; background: linear-gradient(135deg, #00ff88, #00d4ff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }\n" +
        "    .stat-card .detail { font-size: 12px; color: #a0a0a0; margin-top: 5px; }\n" +
        "    .filter-panel { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; margin-bottom: 30px; display: flex; gap: 20px; flex-wrap: wrap; align-items: center; }\n" +
        "    .filter-group { display: flex; gap: 15px; align-items: center; }\n" +
        "    .filter-group label { font-size: 14px; color: #a0a0a0; }\n" +
        "    .checkbox-group { display: flex; gap: 10px; flex-wrap: wrap; }\n" +
        "    .checkbox-group label { display: flex; align-items: center; gap: 5px; cursor: pointer; padding: 5px 15px; background: rgba(45, 53, 97, 0.5); border-radius: 20px; transition: all 0.3s; font-size: 13px; }\n" +
        "    .checkbox-group label:hover { background: rgba(45, 53, 97, 0.8); }\n" +
        "    .image-gallery { display: grid; grid-template-columns: repeat(auto-fill, minmax(400px, 1fr)); gap: 30px; margin-bottom: 30px; }\n" +
        "    .match-card { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4); transition: transform 0.3s, box-shadow 0.3s; cursor: pointer; }\n" +
        "    .match-card:hover { transform: translateY(-5px); box-shadow: 0 12px 48px rgba(0, 255, 136, 0.2); }\n" +
        "    .match-card.success { border-left: 4px solid #00ff88; }\n" +
        "    .match-card.failure { border-left: 4px solid #ff0055; }\n" +
        "    .match-image { width: 100%%; height: auto; border-radius: 8px; margin-bottom: 15px; border: 1px solid #2d3561; cursor: zoom-in; }\n" +
        "    .match-details { display: flex; flex-direction: column; gap: 10px; }\n" +
        "    .match-header { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; }\n" +
        "    .match-title { font-size: 16px; font-weight: bold; color: #e0e0e0; }\n" +
        "    .status-badge { padding: 5px 15px; border-radius: 20px; font-size: 12px; font-weight: bold; text-transform: uppercase; }\n" +
        "    .status-badge.success { background: #00ff88; color: #1a1a2e; box-shadow: 0 0 20px rgba(0, 255, 136, 0.5); }\n" +
        "    .status-badge.failure { background: #ff0055; color: #ffffff; box-shadow: 0 0 20px rgba(255, 0, 85, 0.5); }\n" +
        "    .info-grid { display: grid; grid-template-columns: auto 1fr; gap: 5px 15px; font-size: 13px; }\n" +
        "    .info-label { color: #a0a0a0; }\n" +
        "    .info-value { color: #e0e0e0; font-family: 'Courier New', monospace; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }\n" +
        "    .method-tag { display: inline-block; padding: 3px 10px; border-radius: 12px; font-size: 11px; font-weight: bold; margin-right: 5px; }\n" +
        "    .method-tag.opencv { background: rgba(0, 212, 255, 0.2); border: 1px solid #00d4ff; color: #00d4ff; }\n" +
        "    .method-tag.ocr { background: rgba(255, 170, 0, 0.2); border: 1px solid #ffaa00; color: #ffaa00; }\n" +
        "    .color-legend { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; margin-bottom: 30px; }\n" +
        "    .color-legend h3 { font-size: 18px; margin-bottom: 15px; color: #e0e0e0; }\n" +
        "    .legend-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }\n" +
        "    .legend-section h4 { font-size: 14px; color: #00ff88; margin-bottom: 10px; text-transform: uppercase; }\n" +
        "    .legend-item { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; font-size: 13px; }\n" +
        "    .color-box { width: 20px; height: 20px; border-radius: 4px; border: 1px solid #2d3561; flex-shrink: 0; }\n" +
        "    .color-box.green { background: #00ff88; }\n" +
        "    .color-box.yellow { background: #ffaa00; }\n" +
        "    .color-box.red { background: #ff0055; }\n" +
        "    .color-box.blue { background: #0055ff; }\n" +
        "    .color-box.orange { background: #ff6600; }\n" +
        "    .modal { display: none; position: fixed; top: 0; left: 0; width: 100%%; height: 100%%; background: rgba(0, 0, 0, 0.95); z-index: 1000; padding: 40px; }\n" +
        "    .modal.active { display: flex; align-items: center; justify-content: center; }\n" +
        "    .modal-content { max-width: 90%%; max-height: 90%%; position: relative; }\n" +
        "    .modal-content img { width: 100%%; height: auto; border-radius: 12px; cursor: zoom-out; }\n" +
        "    .close-modal { position: absolute; top: 20px; right: 40px; font-size: 40px; color: #ffffff; cursor: pointer; background: rgba(255, 0, 85, 0.8); width: 50px; height: 50px; border-radius: 50%%; display: flex; align-items: center; justify-content: center; line-height: 1; transition: all 0.3s; z-index: 1001; }\n" +
        "    .close-modal:hover { background: rgba(255, 0, 85, 1); transform: scale(1.1); }\n" +
        "    .empty-state { text-align: center; padding: 60px 20px; background: rgba(22, 33, 62, 0.8); border-radius: 12px; border: 1px solid #2d3561; }\n" +
        "    .empty-state h2 { font-size: 24px; color: #a0a0a0; margin-bottom: 10px; }\n" +
        "    .empty-state p { color: #707080; }\n" +
        "  </style>\n" +
        "</head>\n" +
        "<body>\n" +
        "  <div class=\"container\">\n" +
        "    <header>\n" +
        "      <h1>\uD83D\uDD0D Debug Matches Viewer</h1>\n" +
        "      <div class=\"scenario-info\">\n" +
        "        <span>Scenarios: %s</span>\n" +
        "        <span>%d matches</span>\n" +
        "        <span>Generated: %s</span>\n" +
        "      </div>\n" +
        "    </header>\n" +
        "    <div class=\"stats-grid\">\n" +
        "      <div class=\"stat-card\"><h3>Total Matches</h3><div class=\"value\" id=\"stat-total\">0</div><div class=\"detail\" id=\"stat-success-rate\">0%% success rate</div></div>\n" +
        "      <div class=\"stat-card\"><h3>OpenCV</h3><div class=\"value\" id=\"stat-opencv\">0</div><div class=\"detail\" id=\"stat-opencv-detail\">0 success, 0 failed</div></div>\n" +
        "      <div class=\"stat-card\"><h3>OCR</h3><div class=\"value\" id=\"stat-ocr\">0</div><div class=\"detail\" id=\"stat-ocr-detail\">0 success, 0 failed</div></div>\n" +
        "      <div class=\"stat-card\"><h3>Avg Confidence</h3><div class=\"value\" id=\"stat-confidence\">-</div><div class=\"detail\">OpenCV only</div></div>\n" +
        "    </div>\n" +
        "    <div class=\"color-legend\">\n" +
        "      <h3>\uD83C\uDFA8 Rectangle Color Guide</h3>\n" +
        "      <div class=\"legend-grid\">\n" +
        "        <div class=\"legend-section\"><h4>OpenCV Template Matching</h4>\n" +
        "          <div class=\"legend-item\"><div class=\"color-box green\"></div><span>Green: Excellent (≥0.9)</span></div>\n" +
        "          <div class=\"legend-item\"><div class=\"color-box yellow\"></div><span>Yellow: Good (0.8-0.9)</span></div>\n" +
        "          <div class=\"legend-item\"><div class=\"color-box red\"></div><span>Red: Failed (&lt;0.8)</span></div>\n" +
        "        </div>\n" +
        "        <div class=\"legend-section\"><h4>OCR Detection</h4>\n" +
        "          <div class=\"legend-item\"><div class=\"color-box blue\"></div><span>Blue: Label Text</span></div>\n" +
        "          <div class=\"legend-item\"><div class=\"color-box green\"></div><span>Green: Hint/Value Text</span></div>\n" +
        "          <div class=\"legend-item\"><div class=\"color-box orange\"></div><span>Orange: Color Region</span></div>\n" +
        "          <div class=\"legend-item\"><div class=\"color-box red\"></div><span>Red: Click Point</span></div>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "    </div>\n" +
        "    <div class=\"filter-panel\">\n" +
        "      <div class=\"filter-group\"><label>Show:</label>\n" +
        "        <div class=\"checkbox-group\">\n" +
        "          <label><input type=\"checkbox\" checked data-filter=\"success\"><span>✅ Success</span></label>\n" +
        "          <label><input type=\"checkbox\" checked data-filter=\"failure\"><span>❌ Failures</span></label>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "      <div class=\"filter-group\"><label>Method:</label>\n" +
        "        <div class=\"checkbox-group\">\n" +
        "          <label><input type=\"checkbox\" checked data-filter=\"opencv\"><span>OpenCV</span></label>\n" +
        "          <label><input type=\"checkbox\" checked data-filter=\"ocr\"><span>OCR</span></label>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "    </div>\n" +
        "    <div class=\"image-gallery\" id=\"gallery\"></div>\n" +
        "  </div>\n" +
        "  <div class=\"modal\" id=\"modal\"><span class=\"close-modal\" onclick=\"closeModal()\">×</span><div class=\"modal-content\"><img id=\"modal-image\" src=\"\" alt=\"Debug Image\"></div></div>\n" +
        "  <script>\n" +
        "    const matchData = %s;\n" +
        "    function renderGallery(filters) { const gallery = document.getElementById('gallery'); gallery.innerHTML = ''; const filtered = matchData.filter(match => { if (!filters.success && match.status === 'success') return false; if (!filters.failure && match.status === 'failure') return false; if (!filters.opencv && match.method === 'opencv') return false; if (!filters.ocr && match.method === 'ocr') return false; return true; }); if (filtered.length === 0) { gallery.innerHTML = '<div class=\"empty-state\"><h2>No matches found</h2><p>Try adjusting your filters or run tests with debug mode enabled.</p></div>'; return; } filtered.forEach(match => { const card = createMatchCard(match); gallery.appendChild(card); }); }\n" +
        "    function createMatchCard(match) { const card = document.createElement('div'); card.className = `match-card ${match.status}`; const confidenceHtml = match.confidence !== null ? `<span class=\"info-label\">Confidence:</span><span class=\"info-value\">${match.confidence.toFixed(2)}</span>` : ''; card.innerHTML = `<img src=\"${match.path}\" class=\"match-image\" onclick=\"openModal('${match.path}')\" alt=\"${match.element}\"><div class=\"match-details\"><div class=\"match-header\"><div class=\"match-title\">${match.element.replace(/_/g, ' ')}</div><div class=\"status-badge ${match.status}\">${match.status === 'success' ? '✅ SUCCESS' : '❌ FAIL'}</div></div><div class=\"info-grid\"><span class=\"info-label\">Method:</span><span class=\"info-value\"><span class=\"method-tag ${match.method}\">${match.method.toUpperCase()}</span></span><span class=\"info-label\">Strategy:</span><span class=\"info-value\">${match.strategy}</span>${confidenceHtml}<span class=\"info-label\">Timestamp:</span><span class=\"info-value\">${match.timestamp}</span><span class=\"info-label\">Scenario:</span><span class=\"info-value\" title=\"${match.scenario}\">${match.scenario}</span></div></div>`; return card; }\n" +
        "    function openModal(imagePath) { document.getElementById('modal-image').src = imagePath; document.getElementById('modal').classList.add('active'); }\n" +
        "    function closeModal() { document.getElementById('modal').classList.remove('active'); }\n" +
        "    let currentFilters = { success: true, failure: true, opencv: true, ocr: true };\n" +
        "    document.querySelectorAll('[data-filter]').forEach(checkbox => { checkbox.addEventListener('change', (e) => { currentFilters[e.target.dataset.filter] = e.target.checked; renderGallery(currentFilters); }); });\n" +
        "    function updateStats() { const total = matchData.length; const successes = matchData.filter(m => m.status === 'success').length; const opencvMatches = matchData.filter(m => m.method === 'opencv'); const ocrMatches = matchData.filter(m => m.method === 'ocr'); document.getElementById('stat-total').textContent = total; document.getElementById('stat-success-rate').textContent = total > 0 ? `${((successes/total)*100).toFixed(1)}%% success rate` : 'N/A'; document.getElementById('stat-opencv').textContent = opencvMatches.length; document.getElementById('stat-opencv-detail').textContent = `${opencvMatches.filter(m => m.status === 'success').length} success, ${opencvMatches.filter(m => m.status === 'failure').length} failed`; document.getElementById('stat-ocr').textContent = ocrMatches.length; document.getElementById('stat-ocr-detail').textContent = `${ocrMatches.filter(m => m.status === 'success').length} success, ${ocrMatches.filter(m => m.status === 'failure').length} failed`; const opencvWithConf = opencvMatches.filter(m => m.confidence !== null); if (opencvWithConf.length > 0) { const avgConf = opencvWithConf.reduce((sum, m) => sum + m.confidence, 0) / opencvWithConf.length; document.getElementById('stat-confidence').textContent = avgConf.toFixed(2); } else { document.getElementById('stat-confidence').textContent = 'N/A'; } }\n" +
        "    renderGallery(currentFilters); updateStats();\n" +
        "    document.addEventListener('keydown', (e) => { if (e.key === 'Escape') closeModal(); });\n" +
        "    document.getElementById('modal').addEventListener('click', (e) => { if (e.target.id === 'modal') closeModal(); });\n" +
        "  </script>\n" +
        "</body>\n" +
        "</html>";

    // HTML template with embedded match data (no fetch required, works with file://)
    private static final String HTML_TEMPLATE_EMBEDDED =
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "  <meta charset=\"UTF-8\">\n" +
        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
        "  <title>🔍 Debug Matches Viewer (Last 7 Days)</title>\n" +
        "  <style>\n" +
        "    * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
        "    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #1a1a2e 0%%, #16213e 100%%); color: #e0e0e0; padding: 20px; min-height: 100vh; }\n" +
        "    .container { max-width: 1400px; margin: 0 auto; }\n" +
        "    header { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px 30px; margin-bottom: 30px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4); }\n" +
        "    header h1 { font-size: 32px; margin-bottom: 10px; background: linear-gradient(135deg, #00ff88, #00d4ff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }\n" +
        "    .scenario-info { display: flex; gap: 20px; color: #a0a0a0; font-size: 14px; flex-wrap: wrap; }\n" +
        "    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 30px; }\n" +
        "    .stat-card { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3); }\n" +
        "    .stat-card h3 { font-size: 14px; color: #a0a0a0; margin-bottom: 10px; text-transform: uppercase; letter-spacing: 1px; }\n" +
        "    .stat-card .value { font-size: 36px; font-weight: bold; background: linear-gradient(135deg, #00ff88, #00d4ff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }\n" +
        "    .stat-card .detail { font-size: 12px; color: #a0a0a0; margin-top: 5px; }\n" +
        "    .filter-panel { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; margin-bottom: 30px; display: flex; gap: 20px; flex-wrap: wrap; align-items: center; }\n" +
        "    .filter-group { display: flex; gap: 15px; align-items: center; }\n" +
        "    .filter-group label { font-size: 14px; color: #a0a0a0; }\n" +
        "    .checkbox-group { display: flex; gap: 10px; flex-wrap: wrap; }\n" +
        "    .checkbox-group label { display: flex; align-items: center; gap: 5px; cursor: pointer; padding: 5px 15px; background: rgba(45, 53, 97, 0.5); border-radius: 20px; transition: all 0.3s; font-size: 13px; }\n" +
        "    .checkbox-group label:hover { background: rgba(45, 53, 97, 0.8); }\n" +
        "    .accordion { margin-bottom: 30px; }\n" +
        "    .accordion-item { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; margin-bottom: 15px; overflow: hidden; }\n" +
        "    .accordion-header { padding: 20px; cursor: pointer; display: flex; justify-content: space-between; align-items: center; transition: all 0.3s; user-select: none; }\n" +
        "    .accordion-header:hover { background: rgba(45, 53, 97, 0.3); }\n" +
        "    .accordion-title { font-size: 18px; font-weight: bold; color: #00ff88; }\n" +
        "    .accordion-meta { display: flex; gap: 20px; align-items: center; }\n" +
        "    .accordion-count { font-size: 14px; color: #a0a0a0; }\n" +
        "    .accordion-icon { font-size: 24px; color: #00d4ff; transition: transform 0.3s; }\n" +
        "    .accordion-item.active .accordion-icon { transform: rotate(180deg); }\n" +
        "    .accordion-content { max-height: 0; overflow: hidden; transition: max-height 0.5s ease-out; }\n" +
        "    .accordion-item.active .accordion-content { max-height: 50000px; transition: max-height 1s ease-in; }\n" +
        "    .scenario-tabs { display: flex; gap: 10px; padding: 15px 20px 0 20px; border-bottom: 1px solid #2d3561; flex-wrap: wrap; }\n" +
        "    .scenario-tab { padding: 10px 20px; cursor: pointer; border-radius: 8px 8px 0 0; font-size: 14px; font-weight: 500; transition: all 0.3s; background: rgba(45, 53, 97, 0.3); color: #a0a0a0; border: 1px solid transparent; }\n" +
        "    .scenario-tab:hover { background: rgba(45, 53, 97, 0.5); color: #e0e0e0; }\n" +
        "    .scenario-tab.active { background: rgba(0, 255, 136, 0.15); color: #00ff88; border: 1px solid #00ff88; border-bottom-color: transparent; }\n" +
        "    .scenario-content { display: none; }\n" +
        "    .scenario-content.active { display: block; }\n" +
        "    .image-gallery { display: grid; grid-template-columns: repeat(auto-fill, minmax(400px, 1fr)); gap: 30px; padding: 20px; }\n" +
        "    .match-card { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4); transition: transform 0.3s, box-shadow 0.3s; cursor: pointer; }\n" +
        "    .match-card:hover { transform: translateY(-5px); box-shadow: 0 12px 48px rgba(0, 255, 136, 0.2); }\n" +
        "    .match-card.success { border-left: 4px solid #00ff88; }\n" +
        "    .match-card.failure { border-left: 4px solid #ff0055; }\n" +
        "    .match-image { width: 100%%; height: auto; border-radius: 8px; margin-bottom: 15px; border: 1px solid #2d3561; }\n" +
        "    .image-error { padding: 50px; background: rgba(255, 0, 85, 0.1); border: 2px dashed #ff0055; border-radius: 8px; text-align: center; color: #ff0055; font-size: 14px; margin-bottom: 15px; }\n" +
        "    .match-details { display: flex; flex-direction: column; gap: 10px; }\n" +
        "    .match-header { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; }\n" +
        "    .match-title { font-size: 16px; font-weight: bold; color: #e0e0e0; }\n" +
        "    .status-badge { padding: 5px 15px; border-radius: 20px; font-size: 12px; font-weight: bold; text-transform: uppercase; }\n" +
        "    .status-badge.success { background: #00ff88; color: #1a1a2e; box-shadow: 0 0 20px rgba(0, 255, 136, 0.5); }\n" +
        "    .status-badge.failure { background: #ff0055; color: #ffffff; box-shadow: 0 0 20px rgba(255, 0, 85, 0.5); }\n" +
        "    .info-grid { display: grid; grid-template-columns: auto 1fr; gap: 5px 15px; font-size: 13px; }\n" +
        "    .info-label { color: #a0a0a0; }\n" +
        "    .info-value { color: #e0e0e0; font-family: 'Courier New', monospace; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }\n" +
        "    .method-tag { display: inline-block; padding: 3px 10px; border-radius: 12px; font-size: 11px; font-weight: bold; margin-right: 5px; }\n" +
        "    .method-tag.opencv { background: rgba(0, 212, 255, 0.2); border: 1px solid #00d4ff; color: #00d4ff; }\n" +
        "    .method-tag.ocr { background: rgba(255, 170, 0, 0.2); border: 1px solid #ffaa00; color: #ffaa00; }\n" +
        "    .color-legend { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; margin-bottom: 30px; }\n" +
        "    .color-legend h3 { font-size: 18px; margin-bottom: 15px; color: #e0e0e0; }\n" +
        "    .legend-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }\n" +
        "    .legend-section h4 { font-size: 14px; color: #00ff88; margin-bottom: 10px; text-transform: uppercase; }\n" +
        "    .legend-item { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; font-size: 13px; }\n" +
        "    .color-box { width: 20px; height: 20px; border-radius: 4px; border: 1px solid #2d3561; flex-shrink: 0; }\n" +
        "    .color-box.green { background: #00ff88; }\n" +
        "    .color-box.yellow { background: #ffaa00; }\n" +
        "    .color-box.red { background: #ff0055; }\n" +
        "    .color-box.blue { background: #0055ff; }\n" +
        "    .color-box.orange { background: #ff6600; }\n" +
        "    .empty-state { text-align: center; padding: 60px 20px; background: rgba(22, 33, 62, 0.8); border-radius: 12px; border: 1px solid #2d3561; }\n" +
        "    .empty-state h2 { font-size: 24px; color: #a0a0a0; margin-bottom: 10px; }\n" +
        "    .empty-state p { color: #707080; }\n" +
        "  </style>\n" +
        "</head>\n" +
        "<body>\n" +
        "  <div class=\"container\">\n" +
        "    <header>\n" +
        "      <h1>🔍 Debug Matches Viewer (Last 7 Days)</h1>\n" +
        "      <div class=\"scenario-info\">\n" +
        "        <span>%s</span>\n" +
        "        <span>Generated: %s</span>\n" +
        "      </div>\n" +
        "    </header>\n" +
        "    <div id=\"content\">\n" +
        "      <div class=\"stats-grid\">\n" +
        "        <div class=\"stat-card\"><h3>Total Matches</h3><div class=\"value\" id=\"stat-total\">0</div><div class=\"detail\" id=\"stat-success-rate\">0%% success rate</div></div>\n" +
        "        <div class=\"stat-card\"><h3>OpenCV</h3><div class=\"value\" id=\"stat-opencv\">0</div><div class=\"detail\" id=\"stat-opencv-detail\">0 success, 0 failed</div></div>\n" +
        "        <div class=\"stat-card\"><h3>OCR</h3><div class=\"value\" id=\"stat-ocr\">0</div><div class=\"detail\" id=\"stat-ocr-detail\">0 success, 0 failed</div></div>\n" +
        "        <div class=\"stat-card\"><h3>Avg Confidence</h3><div class=\"value\" id=\"stat-confidence\">-</div><div class=\"detail\">OpenCV only</div></div>\n" +
        "      </div>\n" +
        "      <div class=\"color-legend\">\n" +
        "        <h3>🎨 Rectangle Color Guide</h3>\n" +
        "        <div class=\"legend-grid\">\n" +
        "          <div class=\"legend-section\"><h4>OpenCV Template Matching</h4>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box green\"></div><span>Green: Excellent (≥0.9)</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box yellow\"></div><span>Yellow: Good (0.8-0.9)</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box red\"></div><span>Red: Failed (&lt;0.8)</span></div>\n" +
        "          </div>\n" +
        "          <div class=\"legend-section\"><h4>OCR Detection</h4>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box blue\"></div><span>Blue: Label Text</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box green\"></div><span>Green: Hint/Value Text</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box orange\"></div><span>Orange: Color Region</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box red\"></div><span>Red: Click Point</span></div>\n" +
        "          </div>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "      <div class=\"filter-panel\">\n" +
        "        <div class=\"filter-group\"><label>Show:</label>\n" +
        "          <div class=\"checkbox-group\">\n" +
        "            <label><input type=\"checkbox\" checked data-filter=\"success\"><span>✅ Success</span></label>\n" +
        "            <label><input type=\"checkbox\" checked data-filter=\"failure\"><span>❌ Failures</span></label>\n" +
        "          </div>\n" +
        "        </div>\n" +
        "        <div class=\"filter-group\"><label>Method:</label>\n" +
        "          <div class=\"checkbox-group\">\n" +
        "            <label><input type=\"checkbox\" checked data-filter=\"opencv\"><span>OpenCV</span></label>\n" +
        "            <label><input type=\"checkbox\" checked data-filter=\"ocr\"><span>OCR</span></label>\n" +
        "          </div>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "      <div class=\"accordion\" id=\"accordion\"></div>\n" +
        "    </div>\n" +
        "  </div>\n" +
        "  <script>\n" +
        "    const testRunGroups = %s;\n" +
        "    let currentFilters = { success: true, failure: true, opencv: true, ocr: true };\n" +
        "\n" +
        "    function renderAccordion(filters) {\n" +
        "      const accordion = document.getElementById('accordion');\n" +
        "      accordion.innerHTML = '';\n" +
        "      if (testRunGroups.length === 0) {\n" +
        "        accordion.innerHTML = '<div class=\"empty-state\"><h2>No debug data found</h2><p>Run tests with debug mode enabled to generate match data.</p></div>';\n" +
        "        return;\n" +
        "      }\n" +
        "      testRunGroups.forEach((testRun, runIndex) => {\n" +
        "        const filtered = testRun.matches.filter(match => {\n" +
        "          if (!filters.success && match.status === 'success') return false;\n" +
        "          if (!filters.failure && match.status === 'failure') return false;\n" +
        "          if (!filters.opencv && match.method === 'opencv') return false;\n" +
        "          if (!filters.ocr && match.method === 'ocr') return false;\n" +
        "          return true;\n" +
        "        });\n" +
        "        if (filtered.length === 0) return;\n" +
        "        \n" +
        "        // Group matches by scenario\n" +
        "        const scenarioGroups = {};\n" +
        "        filtered.forEach(match => {\n" +
        "          const scenario = match.scenario || 'Unknown';\n" +
        "          if (!scenarioGroups[scenario]) scenarioGroups[scenario] = [];\n" +
        "          scenarioGroups[scenario].push(match);\n" +
        "        });\n" +
        "        \n" +
        "        const scenarios = Object.keys(scenarioGroups);\n" +
        "        const item = document.createElement('div');\n" +
        "        item.className = 'accordion-item' + (runIndex === 0 ? ' active' : '');\n" +
        "        const header = document.createElement('div');\n" +
        "        header.className = 'accordion-header';\n" +
        "        header.innerHTML = `<div class=\"accordion-title\">📅 Test Run: ${testRun.timestamp}</div><div class=\"accordion-meta\"><span class=\"accordion-count\">${filtered.length} matches, ${scenarios.length} scenario(s)</span><span class=\"accordion-icon\">▼</span></div>`;\n" +
        "        header.onclick = () => item.classList.toggle('active');\n" +
        "        \n" +
        "        const content = document.createElement('div');\n" +
        "        content.className = 'accordion-content';\n" +
        "        \n" +
        "        // Create tabs if multiple scenarios\n" +
        "        if (scenarios.length > 1) {\n" +
        "          const tabsContainer = document.createElement('div');\n" +
        "          tabsContainer.className = 'scenario-tabs';\n" +
        "          scenarios.forEach((scenario, tabIndex) => {\n" +
        "            const tab = document.createElement('div');\n" +
        "            tab.className = 'scenario-tab' + (tabIndex === 0 ? ' active' : '');\n" +
        "            tab.textContent = `🎬 ${scenario.replace(/_/g, ' ')} (${scenarioGroups[scenario].length})`;\n" +
        "            tab.onclick = (e) => {\n" +
        "              e.stopPropagation();\n" +
        "              item.querySelectorAll('.scenario-tab').forEach(t => t.classList.remove('active'));\n" +
        "              item.querySelectorAll('.scenario-content').forEach(c => c.classList.remove('active'));\n" +
        "              tab.classList.add('active');\n" +
        "              item.querySelectorAll('.scenario-content')[tabIndex].classList.add('active');\n" +
        "            };\n" +
        "            tabsContainer.appendChild(tab);\n" +
        "          });\n" +
        "          content.appendChild(tabsContainer);\n" +
        "        }\n" +
        "        \n" +
        "        // Create content for each scenario\n" +
        "        scenarios.forEach((scenario, tabIndex) => {\n" +
        "          const scenarioContent = document.createElement('div');\n" +
        "          scenarioContent.className = 'scenario-content' + (tabIndex === 0 ? ' active' : '');\n" +
        "          const gallery = document.createElement('div');\n" +
        "          gallery.className = 'image-gallery';\n" +
        "          scenarioGroups[scenario].forEach(match => {\n" +
        "            const card = createMatchCard(match);\n" +
        "            gallery.appendChild(card);\n" +
        "          });\n" +
        "          scenarioContent.appendChild(gallery);\n" +
        "          content.appendChild(scenarioContent);\n" +
        "        });\n" +
        "        \n" +
        "        item.appendChild(header);\n" +
        "        item.appendChild(content);\n" +
        "        accordion.appendChild(item);\n" +
        "      });\n" +
        "    }\n" +
        "\n" +
        "    function createMatchCard(match) {\n" +
        "      const card = document.createElement('div');\n" +
        "      card.className = `match-card ${match.status}`;\n" +
        "      const confidenceHtml = match.confidence !== null ? `<span class=\"info-label\">Confidence:</span><span class=\"info-value\">${match.confidence.toFixed(2)}</span>` : '';\n" +
        "      const imagePath = match.path;\n" +
        "      const imageHtml = `<img src=\"${imagePath}\" class=\"match-image\" onerror=\"this.outerHTML='<div class=\\\\'image-error\\\\'>❌ Image deleted (mvn clean)<br/>Metadata preserved for 7 days</div>'\" alt=\"${match.element}\">`;\n" +
        "      card.innerHTML = `${imageHtml}<div class=\"match-details\"><div class=\"match-header\"><div class=\"match-title\">${match.element.replace(/_/g, ' ')}</div><div class=\"status-badge ${match.status}\">${match.status === 'success' ? '✅ SUCCESS' : '❌ FAIL'}</div></div><div class=\"info-grid\"><span class=\"info-label\">Method:</span><span class=\"info-value\"><span class=\"method-tag ${match.method}\">${match.method.toUpperCase()}</span></span><span class=\"info-label\">Strategy:</span><span class=\"info-value\">${match.strategy}</span>${confidenceHtml}<span class=\"info-label\">Timestamp:</span><span class=\"info-value\">${match.timestamp}</span><span class=\"info-label\">Scenario:</span><span class=\"info-value\" title=\"${match.scenario}\">${match.scenario}</span></div></div>`;\n" +
        "      return card;\n" +
        "    }\n" +
        "\n" +
        "    document.querySelectorAll('[data-filter]').forEach(checkbox => {\n" +
        "      checkbox.addEventListener('change', (e) => {\n" +
        "        currentFilters[e.target.dataset.filter] = e.target.checked;\n" +
        "        renderAccordion(currentFilters);\n" +
        "      });\n" +
        "    });\n" +
        "\n" +
        "    function updateStats() {\n" +
        "      const allMatches = testRunGroups.flatMap(run => run.matches);\n" +
        "      const total = allMatches.length;\n" +
        "      const successes = allMatches.filter(m => m.status === 'success').length;\n" +
        "      const opencvMatches = allMatches.filter(m => m.method === 'opencv');\n" +
        "      const ocrMatches = allMatches.filter(m => m.method === 'ocr');\n" +
        "      document.getElementById('stat-total').textContent = total;\n" +
        "      document.getElementById('stat-success-rate').textContent = total > 0 ? `${((successes/total)*100).toFixed(1)}%% success rate` : 'N/A';\n" +
        "      document.getElementById('stat-opencv').textContent = opencvMatches.length;\n" +
        "      document.getElementById('stat-opencv-detail').textContent = `${opencvMatches.filter(m => m.status === 'success').length} success, ${opencvMatches.filter(m => m.status === 'failure').length} failed`;\n" +
        "      document.getElementById('stat-ocr').textContent = ocrMatches.length;\n" +
        "      document.getElementById('stat-ocr-detail').textContent = `${ocrMatches.filter(m => m.status === 'success').length} success, ${ocrMatches.filter(m => m.status === 'failure').length} failed`;\n" +
        "      const opencvWithConf = opencvMatches.filter(m => m.confidence !== null);\n" +
        "      if (opencvWithConf.length > 0) {\n" +
        "        const avgConf = opencvWithConf.reduce((sum, m) => sum + m.confidence, 0) / opencvWithConf.length;\n" +
        "        document.getElementById('stat-confidence').textContent = avgConf.toFixed(2);\n" +
        "      } else {\n" +
        "        document.getElementById('stat-confidence').textContent = 'N/A';\n" +
        "      }\n" +
        "    }\n" +
        "\n" +
        "    if (testRunGroups.length > 0) {\n" +
        "      renderAccordion(currentFilters);\n" +
        "      updateStats();\n" +
        "    } else {\n" +
        "      document.getElementById('accordion').innerHTML = '<div class=\"empty-state\"><h2>No debug data found</h2><p>Run tests with debug mode enabled to generate match data.</p></div>';\n" +
        "    }\n" +
        "  </script>\n" +
        "</body>\n" +
        "</html>";

    // New HTML template that loads JSON files dynamically (deprecated due to CORS issues with file://)
    @Deprecated
    private static final String HTML_TEMPLATE_DYNAMIC =
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "  <meta charset=\"UTF-8\">\n" +
        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
        "  <title>\uD83D\uDD0D Debug Matches Viewer (Last 7 Days)</title>\n" +
        "  <style>\n" +
        "    * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
        "    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #1a1a2e 0%%, #16213e 100%%); color: #e0e0e0; padding: 20px; min-height: 100vh; }\n" +
        "    .container { max-width: 1400px; margin: 0 auto; }\n" +
        "    header { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px 30px; margin-bottom: 30px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4); }\n" +
        "    header h1 { font-size: 32px; margin-bottom: 10px; background: linear-gradient(135deg, #00ff88, #00d4ff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }\n" +
        "    .scenario-info { display: flex; gap: 20px; color: #a0a0a0; font-size: 14px; flex-wrap: wrap; }\n" +
        "    .loading { text-align: center; padding: 60px; font-size: 18px; color: #a0a0a0; }\n" +
        "    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 30px; }\n" +
        "    .stat-card { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3); }\n" +
        "    .stat-card h3 { font-size: 14px; color: #a0a0a0; margin-bottom: 10px; text-transform: uppercase; letter-spacing: 1px; }\n" +
        "    .stat-card .value { font-size: 36px; font-weight: bold; background: linear-gradient(135deg, #00ff88, #00d4ff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }\n" +
        "    .stat-card .detail { font-size: 12px; color: #a0a0a0; margin-top: 5px; }\n" +
        "    .filter-panel { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; margin-bottom: 30px; display: flex; gap: 20px; flex-wrap: wrap; align-items: center; }\n" +
        "    .filter-group { display: flex; gap: 15px; align-items: center; }\n" +
        "    .filter-group label { font-size: 14px; color: #a0a0a0; }\n" +
        "    .checkbox-group { display: flex; gap: 10px; flex-wrap: wrap; }\n" +
        "    .checkbox-group label { display: flex; align-items: center; gap: 5px; cursor: pointer; padding: 5px 15px; background: rgba(45, 53, 97, 0.5); border-radius: 20px; transition: all 0.3s; font-size: 13px; }\n" +
        "    .checkbox-group label:hover { background: rgba(45, 53, 97, 0.8); }\n" +
        "    .image-gallery { display: grid; grid-template-columns: repeat(auto-fill, minmax(400px, 1fr)); gap: 30px; margin-bottom: 30px; }\n" +
        "    .match-card { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4); transition: transform 0.3s, box-shadow 0.3s; cursor: pointer; }\n" +
        "    .match-card:hover { transform: translateY(-5px); box-shadow: 0 12px 48px rgba(0, 255, 136, 0.2); }\n" +
        "    .match-card.success { border-left: 4px solid #00ff88; }\n" +
        "    .match-card.failure { border-left: 4px solid #ff0055; }\n" +
        "    .match-image { width: 100%%; height: auto; border-radius: 8px; margin-bottom: 15px; border: 1px solid #2d3561; cursor: zoom-in; object-fit: contain; }\n" +
        "    .image-error { padding: 50px; background: rgba(255, 0, 85, 0.1); border: 2px dashed #ff0055; border-radius: 8px; text-align: center; color: #ff0055; font-size: 14px; margin-bottom: 15px; }\n" +
        "    .match-details { display: flex; flex-direction: column; gap: 10px; }\n" +
        "    .match-header { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; }\n" +
        "    .match-title { font-size: 16px; font-weight: bold; color: #e0e0e0; }\n" +
        "    .status-badge { padding: 5px 15px; border-radius: 20px; font-size: 12px; font-weight: bold; text-transform: uppercase; }\n" +
        "    .status-badge.success { background: #00ff88; color: #1a1a2e; box-shadow: 0 0 20px rgba(0, 255, 136, 0.5); }\n" +
        "    .status-badge.failure { background: #ff0055; color: #ffffff; box-shadow: 0 0 20px rgba(255, 0, 85, 0.5); }\n" +
        "    .info-grid { display: grid; grid-template-columns: auto 1fr; gap: 5px 15px; font-size: 13px; }\n" +
        "    .info-label { color: #a0a0a0; }\n" +
        "    .info-value { color: #e0e0e0; font-family: 'Courier New', monospace; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }\n" +
        "    .method-tag { display: inline-block; padding: 3px 10px; border-radius: 12px; font-size: 11px; font-weight: bold; margin-right: 5px; }\n" +
        "    .method-tag.opencv { background: rgba(0, 212, 255, 0.2); border: 1px solid #00d4ff; color: #00d4ff; }\n" +
        "    .method-tag.ocr { background: rgba(255, 170, 0, 0.2); border: 1px solid #ffaa00; color: #ffaa00; }\n" +
        "    .color-legend { background: rgba(22, 33, 62, 0.8); backdrop-filter: blur(10px); border: 1px solid #2d3561; border-radius: 12px; padding: 20px; margin-bottom: 30px; }\n" +
        "    .color-legend h3 { font-size: 18px; margin-bottom: 15px; color: #e0e0e0; }\n" +
        "    .legend-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }\n" +
        "    .legend-section h4 { font-size: 14px; color: #00ff88; margin-bottom: 10px; text-transform: uppercase; }\n" +
        "    .legend-item { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; font-size: 13px; }\n" +
        "    .color-box { width: 20px; height: 20px; border-radius: 4px; border: 1px solid #2d3561; flex-shrink: 0; }\n" +
        "    .color-box.green { background: #00ff88; }\n" +
        "    .color-box.yellow { background: #ffaa00; }\n" +
        "    .color-box.red { background: #ff0055; }\n" +
        "    .color-box.blue { background: #0055ff; }\n" +
        "    .color-box.orange { background: #ff6600; }\n" +
        "    .modal { display: none; position: fixed; top: 0; left: 0; width: 100%%; height: 100%%; background: rgba(0, 0, 0, 0.95); z-index: 1000; padding: 40px; }\n" +
        "    .modal.active { display: flex; align-items: center; justify-content: center; }\n" +
        "    .modal-content { max-width: 90%%; max-height: 90%%; position: relative; }\n" +
        "    .modal-content img { width: 100%%; height: auto; border-radius: 12px; cursor: zoom-out; }\n" +
        "    .close-modal { position: absolute; top: 20px; right: 40px; font-size: 40px; color: #ffffff; cursor: pointer; background: rgba(255, 0, 85, 0.8); width: 50px; height: 50px; border-radius: 50%%; display: flex; align-items: center; justify-content: center; line-height: 1; transition: all 0.3s; z-index: 1001; }\n" +
        "    .close-modal:hover { background: rgba(255, 0, 85, 1); transform: scale(1.1); }\n" +
        "    .empty-state { text-align: center; padding: 60px 20px; background: rgba(22, 33, 62, 0.8); border-radius: 12px; border: 1px solid #2d3561; }\n" +
        "    .empty-state h2 { font-size: 24px; color: #a0a0a0; margin-bottom: 10px; }\n" +
        "    .empty-state p { color: #707080; }\n" +
        "  </style>\n" +
        "</head>\n" +
        "<body>\n" +
        "  <div class=\"container\">\n" +
        "    <header>\n" +
        "      <h1>\uD83D\uDD0D Debug Matches Viewer (Last 7 Days)</h1>\n" +
        "      <div class=\"scenario-info\">\n" +
        "        <span id=\"data-range\">Loading...</span>\n" +
        "        <span>Generated: %s</span>\n" +
        "      </div>\n" +
        "    </header>\n" +
        "    <div class=\"loading\" id=\"loading\">Loading debug match data...</div>\n" +
        "    <div id=\"content\" style=\"display:none;\">\n" +
        "      <div class=\"stats-grid\">\n" +
        "        <div class=\"stat-card\"><h3>Total Matches</h3><div class=\"value\" id=\"stat-total\">0</div><div class=\"detail\" id=\"stat-success-rate\">0%% success rate</div></div>\n" +
        "        <div class=\"stat-card\"><h3>OpenCV</h3><div class=\"value\" id=\"stat-opencv\">0</div><div class=\"detail\" id=\"stat-opencv-detail\">0 success, 0 failed</div></div>\n" +
        "        <div class=\"stat-card\"><h3>OCR</h3><div class=\"value\" id=\"stat-ocr\">0</div><div class=\"detail\" id=\"stat-ocr-detail\">0 success, 0 failed</div></div>\n" +
        "        <div class=\"stat-card\"><h3>Avg Confidence</h3><div class=\"value\" id=\"stat-confidence\">-</div><div class=\"detail\">OpenCV only</div></div>\n" +
        "      </div>\n" +
        "      <div class=\"color-legend\">\n" +
        "        <h3>\uD83C\uDFA8 Rectangle Color Guide</h3>\n" +
        "        <div class=\"legend-grid\">\n" +
        "          <div class=\"legend-section\"><h4>OpenCV Template Matching</h4>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box green\"></div><span>Green: Excellent (≥0.9)</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box yellow\"></div><span>Yellow: Good (0.8-0.9)</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box red\"></div><span>Red: Failed (&lt;0.8)</span></div>\n" +
        "          </div>\n" +
        "          <div class=\"legend-section\"><h4>OCR Detection</h4>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box blue\"></div><span>Blue: Label Text</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box green\"></div><span>Green: Hint/Value Text</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box orange\"></div><span>Orange: Color Region</span></div>\n" +
        "            <div class=\"legend-item\"><div class=\"color-box red\"></div><span>Red: Click Point</span></div>\n" +
        "          </div>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "      <div class=\"filter-panel\">\n" +
        "        <div class=\"filter-group\"><label>Show:</label>\n" +
        "          <div class=\"checkbox-group\">\n" +
        "            <label><input type=\"checkbox\" checked data-filter=\"success\"><span>✅ Success</span></label>\n" +
        "            <label><input type=\"checkbox\" checked data-filter=\"failure\"><span>❌ Failures</span></label>\n" +
        "          </div>\n" +
        "        </div>\n" +
        "        <div class=\"filter-group\"><label>Method:</label>\n" +
        "          <div class=\"checkbox-group\">\n" +
        "            <label><input type=\"checkbox\" checked data-filter=\"opencv\"><span>OpenCV</span></label>\n" +
        "            <label><input type=\"checkbox\" checked data-filter=\"ocr\"><span>OCR</span></label>\n" +
        "          </div>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "      <div class=\"image-gallery\" id=\"gallery\"></div>\n" +
        "    </div>\n" +
        "  </div>\n" +
        "  <div class=\"modal\" id=\"modal\"><span class=\"close-modal\" onclick=\"closeModal()\">×</span><div class=\"modal-content\"><img id=\"modal-image\" src=\"\" alt=\"Debug Image\"></div></div>\n" +
        "  <script>\n" +
        "    let allMatchData = [];\n" +
        "\n" +
        "    // Load all JSON files from metadata folder using manifest\n" +
        "    async function loadMatchData() {\n" +
        "      try {\n" +
        "        const metadataFolder = '../debug_matches_metadata';\n" +
        "        \n" +
        "        // Load manifest file to get list of available JSON files\n" +
        "        const manifestResponse = await fetch(`${metadataFolder}/manifest.json`);\n" +
        "        if (!manifestResponse.ok) {\n" +
        "          throw new Error('Manifest file not found. No test data available yet.');\n" +
        "        }\n" +
        "        \n" +
        "        const manifest = await manifestResponse.json();\n" +
        "        const files = manifest.files || [];\n" +
        "        \n" +
        "        if (files.length === 0) {\n" +
        "          document.getElementById('loading').innerHTML = '<div class=\"empty-state\"><h2>No debug data found</h2><p>Run tests with debug mode enabled to generate match data.</p></div>';\n" +
        "          return;\n" +
        "        }\n" +
        "        \n" +
        "        console.log(`Loading ${files.length} data files from manifest...`);\n" +
        "        \n" +
        "        // Load each JSON file listed in manifest\n" +
        "        for (const file of files) {\n" +
        "          try {\n" +
        "            const response = await fetch(`${metadataFolder}/${file}`);\n" +
        "            if (response.ok) {\n" +
        "              const data = await response.json();\n" +
        "              allMatchData = allMatchData.concat(data);\n" +
        "              console.log(`Loaded ${data.length} matches from ${file}`);\n" +
        "            }\n" +
        "          } catch (e) {\n" +
        "            console.warn(`Could not load ${file}:`, e.message);\n" +
        "          }\n" +
        "        }\n" +
        "        \n" +
        "        if (allMatchData.length === 0) {\n" +
        "          document.getElementById('loading').innerHTML = '<div class=\"empty-state\"><h2>No debug data found</h2><p>Manifest exists but no valid match data available.</p></div>';\n" +
        "          return;\n" +
        "        }\n" +
        "        \n" +
        "        console.log(`Total matches loaded: ${allMatchData.length}`);\n" +
        "        \n" +
        "        // Update data range info\n" +
        "        const timestamps = allMatchData.map(m => m.timestamp).filter(t => t);\n" +
        "        if (timestamps.length > 0) {\n" +
        "          const sorted = timestamps.sort();\n" +
        "          document.getElementById('data-range').textContent = `${allMatchData.length} matches from ${files.length} test runs (Last updated: ${manifest.lastUpdated || 'unknown'})`;\n" +
        "        }\n" +
        "        \n" +
        "        document.getElementById('loading').style.display = 'none';\n" +
        "        document.getElementById('content').style.display = 'block';\n" +
        "        \n" +
        "        renderGallery(currentFilters);\n" +
        "        updateStats();\n" +
        "      } catch (error) {\n" +
        "        console.error('Error loading match data:', error);\n" +
        "        document.getElementById('loading').innerHTML = '<div class=\"empty-state\"><h2>Error loading data</h2><p>' + error.message + '</p><p>Make sure you have run tests with debug mode enabled.</p></div>';\n" +
        "      }\n" +
        "    }\n" +
        "\n" +
        "\n" +
        "    function renderGallery(filters) {\n" +
        "      const gallery = document.getElementById('gallery');\n" +
        "      gallery.innerHTML = '';\n" +
        "      const filtered = allMatchData.filter(match => {\n" +
        "        if (!filters.success && match.status === 'success') return false;\n" +
        "        if (!filters.failure && match.status === 'failure') return false;\n" +
        "        if (!filters.opencv && match.method === 'opencv') return false;\n" +
        "        if (!filters.ocr && match.method === 'ocr') return false;\n" +
        "        return true;\n" +
        "      });\n" +
        "      if (filtered.length === 0) {\n" +
        "        gallery.innerHTML = '<div class=\"empty-state\"><h2>No matches found</h2><p>Try adjusting your filters.</p></div>';\n" +
        "        return;\n" +
        "      }\n" +
        "      filtered.forEach(match => {\n" +
        "        const card = createMatchCard(match);\n" +
        "        gallery.appendChild(card);\n" +
        "      });\n" +
        "    }\n" +
        "\n" +
        "    function createMatchCard(match) {\n" +
        "      const card = document.createElement('div');\n" +
        "      card.className = `match-card ${match.status}`;\n" +
        "      const confidenceHtml = match.confidence !== null ? `<span class=\"info-label\">Confidence:</span><span class=\"info-value\">${match.confidence.toFixed(2)}</span>` : '';\n" +
        "      \n" +
        "      // Image path handling - images deleted after mvn clean but metadata persists\n" +
        "      const imagePath = `../${match.path}`;\n" +
        "      const imageHtml = `<img src=\"${imagePath}\" class=\"match-image\" onerror=\"this.outerHTML='<div class=\\'image-error\\'>❌ Image deleted (mvn clean)<br/>Metadata preserved for 7 days</div>'\" onclick=\"openModal('${imagePath}')\" alt=\"${match.element}\">`;\n" +
        "      \n" +
        "      card.innerHTML = `${imageHtml}<div class=\"match-details\"><div class=\"match-header\"><div class=\"match-title\">${match.element.replace(/_/g, ' ')}</div><div class=\"status-badge ${match.status}\">${match.status === 'success' ? '✅ SUCCESS' : '❌ FAIL'}</div></div><div class=\"info-grid\"><span class=\"info-label\">Method:</span><span class=\"info-value\"><span class=\"method-tag ${match.method}\">${match.method.toUpperCase()}</span></span><span class=\"info-label\">Strategy:</span><span class=\"info-value\">${match.strategy}</span>${confidenceHtml}<span class=\"info-label\">Timestamp:</span><span class=\"info-value\">${match.timestamp}</span><span class=\"info-label\">Scenario:</span><span class=\"info-value\" title=\"${match.scenario}\">${match.scenario}</span></div></div>`;\n" +
        "      return card;\n" +
        "    }\n" +
        "\n" +
        "    function openModal(imagePath) {\n" +
        "      document.getElementById('modal-image').src = imagePath;\n" +
        "      document.getElementById('modal').classList.add('active');\n" +
        "    }\n" +
        "\n" +
        "    function closeModal() {\n" +
        "      document.getElementById('modal').classList.remove('active');\n" +
        "    }\n" +
        "\n" +
        "    let currentFilters = { success: true, failure: true, opencv: true, ocr: true };\n" +
        "    document.querySelectorAll('[data-filter]').forEach(checkbox => {\n" +
        "      checkbox.addEventListener('change', (e) => {\n" +
        "        currentFilters[e.target.dataset.filter] = e.target.checked;\n" +
        "        renderGallery(currentFilters);\n" +
        "      });\n" +
        "    });\n" +
        "\n" +
        "    function updateStats() {\n" +
        "      const total = allMatchData.length;\n" +
        "      const successes = allMatchData.filter(m => m.status === 'success').length;\n" +
        "      const opencvMatches = allMatchData.filter(m => m.method === 'opencv');\n" +
        "      const ocrMatches = allMatchData.filter(m => m.method === 'ocr');\n" +
        "      document.getElementById('stat-total').textContent = total;\n" +
        "      document.getElementById('stat-success-rate').textContent = total > 0 ? `${((successes/total)*100).toFixed(1)}%% success rate` : 'N/A';\n" +
        "      document.getElementById('stat-opencv').textContent = opencvMatches.length;\n" +
        "      document.getElementById('stat-opencv-detail').textContent = `${opencvMatches.filter(m => m.status === 'success').length} success, ${opencvMatches.filter(m => m.status === 'failure').length} failed`;\n" +
        "      document.getElementById('stat-ocr').textContent = ocrMatches.length;\n" +
        "      document.getElementById('stat-ocr-detail').textContent = `${ocrMatches.filter(m => m.status === 'success').length} success, ${ocrMatches.filter(m => m.status === 'failure').length} failed`;\n" +
        "      const opencvWithConf = opencvMatches.filter(m => m.confidence !== null);\n" +
        "      if (opencvWithConf.length > 0) {\n" +
        "        const avgConf = opencvWithConf.reduce((sum, m) => sum + m.confidence, 0) / opencvWithConf.length;\n" +
        "        document.getElementById('stat-confidence').textContent = avgConf.toFixed(2);\n" +
        "      } else {\n" +
        "        document.getElementById('stat-confidence').textContent = 'N/A';\n" +
        "      }\n" +
        "    }\n" +
        "\n" +
        "    document.addEventListener('keydown', (e) => { if (e.key === 'Escape') closeModal(); });\n" +
        "    document.getElementById('modal').addEventListener('click', (e) => { if (e.target.id === 'modal') closeModal(); });\n" +
        "    \n" +
        "    // Load data on page load\n" +
        "    loadMatchData();\n" +
        "  </script>\n" +
        "</body>\n" +
        "</html>";

    /**
     * CLI entry point for testing
     */
    public static void main(String[] args) {
        String folder = args.length > 0 ? args[0] : "screenshots/debug_matches";
        generate(folder);
    }
}
