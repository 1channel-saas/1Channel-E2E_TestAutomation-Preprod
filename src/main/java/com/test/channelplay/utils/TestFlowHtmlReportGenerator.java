package com.test.channelplay.utils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFlowHtmlReportGenerator {

    private static final String SCREENSHOTS_DIR = "screenshots/test_flow";
    private static final String REPORTS_DIR = "target/test-flow-reports";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        new TestFlowHtmlReportGenerator().generateReport();
    }

    public void generateReport() {
        try {
            System.out.println("Generating Test Flow HTML Report...");

            // Create reports directory
            Files.createDirectories(Paths.get(REPORTS_DIR));

            // Get all test scenario folders
            List<TestScenario> scenarios = collectScenarios();

            if (scenarios.isEmpty()) {
                System.out.println("No test flow screenshots found to generate report.");
                return;
            }

            // Generate HTML
            String html = generateHtml(scenarios);

            // Write report
            String reportName = "test_flow_report_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".html";
            Path reportPath = Paths.get(REPORTS_DIR, reportName);
            Files.write(reportPath, html.getBytes());

            // Also write as latest
            Path latestPath = Paths.get(REPORTS_DIR, "test_flow_report_latest.html");
            Files.write(latestPath, html.getBytes());

            System.out.println("Report generated successfully:");
            System.out.println("  - " + reportPath.toAbsolutePath());
            System.out.println("  - " + latestPath.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("Failed to generate report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<TestScenario> collectScenarios() throws IOException {
        List<TestScenario> scenarios = new ArrayList<>();
        Path screenshotsPath = Paths.get(SCREENSHOTS_DIR);

        if (!Files.exists(screenshotsPath)) {
            return scenarios;
        }

        try (Stream<Path> folders = Files.list(screenshotsPath)) {
            folders.filter(Files::isDirectory)
                   .forEach(folder -> {
                       try {
                           TestScenario scenario = new TestScenario();
                           scenario.name = folder.getFileName().toString();
                           scenario.timestamp = extractTimestamp(scenario.name);

                           // Collect all images
                           try (Stream<Path> files = Files.list(folder)) {
                               scenario.screenshots = files
                                   .filter(f -> f.toString().endsWith(".png"))
                                   .sorted()
                                   .map(f -> {
                                       Screenshot shot = new Screenshot();
                                       shot.fileName = f.getFileName().toString();
                                       shot.path = f.toString().replace("\\", "/");
                                       shot.type = determineType(shot.fileName);
                                       shot.stepNumber = extractStepNumber(shot.fileName);
                                       return shot;
                                   })
                                   .collect(Collectors.toList());
                           }

                           if (!scenario.screenshots.isEmpty()) {
                               scenarios.add(scenario);
                           }

                       } catch (Exception e) {
                           System.err.println("Error processing folder: " + folder + " - " + e.getMessage());
                       }
                   });
        }

        // Sort scenarios by timestamp (newest first)
        scenarios.sort((a, b) -> b.timestamp.compareTo(a.timestamp));

        return scenarios;
    }

    private String extractTimestamp(String folderName) {
        // Format: 2025-09-28_02-07-56_Add_OffSide_Activity_Test
        if (folderName.length() >= 19) {
            return folderName.substring(0, 19);
        }
        return folderName;
    }

    private String determineType(String fileName) {
        if (fileName.contains("_app_") || fileName.contains("_mobile_")) {
            return "mobile";
        } else if (fileName.contains("_web_")) {
            return "web";
        } else if (fileName.contains("_manual_")) {
            return "manual";
        }
        return "auto";
    }

    private int extractStepNumber(String fileName) {
        try {
            if (fileName.startsWith("step_")) {
                String numStr = fileName.substring(5, 8);
                return Integer.parseInt(numStr);
            }
        } catch (Exception e) {
            // Ignore
        }
        return 999;
    }

    private String generateHtml(List<TestScenario> scenarios) {
        StringBuilder html = new StringBuilder();

        // HTML Header
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Test Flow Visual Report</title>\n");
        html.append("    <style>\n");
        html.append(getCss());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        // Header
        html.append("    <div class=\"header\">\n");
        html.append("        <h1>Test Flow Visual Report</h1>\n");
        html.append("        <div class=\"timestamp\">Generated: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("</div>\n");
        html.append("    </div>\n");

        // Summary
        html.append("    <div class=\"summary\">\n");
        html.append("        <div class=\"stat-card\">\n");
        html.append("            <div class=\"stat-value\">").append(scenarios.size()).append("</div>\n");
        html.append("            <div class=\"stat-label\">Test Scenarios</div>\n");
        html.append("        </div>\n");

        int totalScreenshots = scenarios.stream().mapToInt(s -> s.screenshots.size()).sum();
        html.append("        <div class=\"stat-card\">\n");
        html.append("            <div class=\"stat-value\">").append(totalScreenshots).append("</div>\n");
        html.append("            <div class=\"stat-label\">Total Screenshots</div>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");

        // Navigation Section with clickable links
        html.append("    <div class=\"navigation-section\">\n");
        html.append("        <h2>Quick Navigation</h2>\n");
        html.append("        <div class=\"nav-subtitle\">Click on any scenario below to jump directly to that section</div>\n");
        html.append("        <div class=\"scenario-links\">\n");

        for (int i = 0; i < scenarios.size(); i++) {
            TestScenario scenario = scenarios.get(i);
            String scenarioName = scenario.name.replaceAll("^\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}_", "");
            html.append("            <a href=\"#scenario-anchor-").append(i).append("\" class=\"scenario-link\" onclick=\"expandAndScroll(").append(i).append("); return false;\">\n");
            html.append("                <span class=\"scenario-number\">").append(i + 1).append("</span>\n");
            html.append("                <span class=\"scenario-name\">").append(scenarioName).append("</span>\n");
            html.append("                <span class=\"scenario-time\">").append(scenario.timestamp).append("</span>\n");
            html.append("            </a>\n");
        }

        html.append("        </div>\n");
        html.append("    </div>\n");

        // Scenarios
        html.append("    <div class=\"container\">\n");

        for (int i = 0; i < scenarios.size(); i++) {
            TestScenario scenario = scenarios.get(i);
            html.append(generateScenarioHtml(scenario, i));
        }

        html.append("    </div>\n");

        // Modal
        html.append("    <div id=\"imageModal\" class=\"modal\">\n");
        html.append("        <span class=\"close\" onclick=\"closeModal()\">&times;</span>\n");
        html.append("        <img class=\"modal-content\" id=\"modalImage\">\n");
        html.append("        <div id=\"caption\"></div>\n");
        html.append("    </div>\n");

        // Go to Top Button
        html.append("    <button id=\"goToTopBtn\" onclick=\"goToTop()\" title=\"Go to top\">\n");
        html.append("        <svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">\n");
        html.append("            <polyline points=\"18 15 12 9 6 15\"></polyline>\n");
        html.append("        </svg>\n");
        html.append("        <span>TOP</span>\n");
        html.append("    </button>\n");

        // Side Navigation Menu
        html.append("    <div id=\"sideNav\" class=\"side-nav\">\n");
        html.append("        <button class=\"toggle-nav\" onclick=\"toggleSideNav()\" title=\"Toggle Navigation\">\n");
        html.append("            <svg width=\"20\" height=\"20\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\">\n");
        html.append("                <line x1=\"3\" y1=\"12\" x2=\"21\" y2=\"12\"></line>\n");
        html.append("                <line x1=\"3\" y1=\"6\" x2=\"21\" y2=\"6\"></line>\n");
        html.append("                <line x1=\"3\" y1=\"18\" x2=\"21\" y2=\"18\"></line>\n");
        html.append("            </svg>\n");
        html.append("        </button>\n");
        html.append("        <div class=\"nav-content\">\n");
        html.append("            <h3>Quick Jump</h3>\n");
        html.append("            <div class=\"nav-links\">\n");

        for (int i = 0; i < Math.min(scenarios.size(), 3); i++) {  // Show max 3 scenarios in side nav
            TestScenario scenario = scenarios.get(i);
            String scenarioName = scenario.name.replaceAll("^\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}_", "");
            if (scenarioName.length() > 25) {
                scenarioName = scenarioName.substring(0, 22) + "...";
            }
            html.append("                <a href=\"#\" onclick=\"expandAndScroll(").append(i).append("); return false;\" class=\"nav-link\">\n");
            html.append("                    <span class=\"nav-num\">").append(i + 1).append("</span>\n");
            html.append("                    <span class=\"nav-text\">").append(scenarioName).append("</span>\n");
            html.append("                </a>\n");
        }

        if (scenarios.size() > 3) {
            html.append("                <div class=\"nav-more\">+").append(scenarios.size() - 3).append(" more...</div>\n");
        }

        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");

        // JavaScript
        html.append("    <script>\n");
        html.append(getJavaScript());
        html.append("    </script>\n");

        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }

    private String generateScenarioHtml(TestScenario scenario, int index) {
        StringBuilder html = new StringBuilder();

        String scenarioName = scenario.name.replaceAll("^\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}_", "");

        html.append("        <div class=\"scenario\" id=\"scenario-anchor-").append(index).append("\">\n");
        html.append("            <div class=\"scenario-header\" onclick=\"toggleScenario(").append(index).append(")\">\n");
        html.append("                <span class=\"toggle-icon\" id=\"toggle-").append(index).append("\">▼</span>\n");
        html.append("                <h2>").append(scenarioName).append("</h2>\n");
        html.append("                <div class=\"scenario-meta\">\n");
        html.append("                    <span class=\"timestamp\">").append(scenario.timestamp).append("</span>\n");
        html.append("                    <span class=\"badge\">").append(scenario.screenshots.size()).append(" screenshots</span>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");

        html.append("            <div class=\"scenario-content\" id=\"scenario-").append(index).append("\">\n");

        // Group screenshots by step for side-by-side view
        Map<Integer, List<Screenshot>> stepGroups = scenario.screenshots.stream()
            .collect(Collectors.groupingBy(s -> s.stepNumber, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Integer, List<Screenshot>> entry : stepGroups.entrySet()) {
            html.append("                <div class=\"step-group\">\n");
            html.append("                    <h3>Step ").append(entry.getKey()).append("</h3>\n");
            html.append("                    <div class=\"screenshots-row\">\n");

            for (Screenshot shot : entry.getValue()) {
                String typeLabel = shot.type.equals("mobile") ? "APP" :
                                  shot.type.equals("web") ? "WEB" :
                                  shot.type.equals("manual") ? "MANUAL" : "AUTO";

                html.append("                        <div class=\"screenshot-card\">\n");
                html.append("                            <div class=\"screenshot-label\">").append(typeLabel).append("</div>\n");
                html.append("                            <img src=\"").append(getImageAsBase64(shot.path))
                    .append("\" onclick=\"openModal(this, 'Step ").append(entry.getKey())
                    .append(" - ").append(typeLabel).append("')\" />\n");
                html.append("                        </div>\n");
            }

            html.append("                    </div>\n");
            html.append("                </div>\n");
        }

        html.append("            </div>\n");
        html.append("        </div>\n");

        return html.toString();
    }

    private String getImageAsBase64(String imagePath) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64;
        } catch (Exception e) {
            return "";
        }
    }

    private String getCss() {
        return "* { margin: 0; padding: 0; box-sizing: border-box; }\n" +
            "body {\n" +
            "    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;\n" +
            "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
            "    min-height: 100vh;\n" +
            "}\n" +
            ".header {\n" +
            "    background: white;\n" +
            "    padding: 30px;\n" +
            "    text-align: center;\n" +
            "    box-shadow: 0 2px 10px rgba(0,0,0,0.1);\n" +
            "}\n" +
            ".header h1 {\n" +
            "    color: #333;\n" +
            "    margin-bottom: 10px;\n" +
            "}\n" +
            ".timestamp {\n" +
            "    color: #666;\n" +
            "    font-size: 14px;\n" +
            "}\n" +
            ".summary {\n" +
            "    display: flex;\n" +
            "    justify-content: center;\n" +
            "    gap: 30px;\n" +
            "    padding: 30px;\n" +
            "}\n" +
            ".stat-card {\n" +
            "    background: white;\n" +
            "    padding: 20px 40px;\n" +
            "    border-radius: 10px;\n" +
            "    box-shadow: 0 4px 6px rgba(0,0,0,0.1);\n" +
            "    text-align: center;\n" +
            "}\n" +
            ".stat-value {\n" +
            "    font-size: 36px;\n" +
            "    font-weight: bold;\n" +
            "    color: #667eea;\n" +
            "}\n" +
            ".stat-label {\n" +
            "    color: #666;\n" +
            "    margin-top: 5px;\n" +
            "}\n" +
            ".navigation-section {\n" +
            "    background: white;\n" +
            "    margin: 30px auto;\n" +
            "    max-width: 1400px;\n" +
            "    padding: 25px;\n" +
            "    border-radius: 10px;\n" +
            "    box-shadow: 0 4px 6px rgba(0,0,0,0.1);\n" +
            "}\n" +
            ".navigation-section h2 {\n" +
            "    color: #333;\n" +
            "    margin-bottom: 8px;\n" +
            "    text-align: center;\n" +
            "}\n" +
            ".nav-subtitle {\n" +
            "    color: #666;\n" +
            "    font-size: 14px;\n" +
            "    text-align: center;\n" +
            "    margin-bottom: 20px;\n" +
            "}\n" +
            ".scenario-links {\n" +
            "    display: grid;\n" +
            "    grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));\n" +
            "    gap: 15px;\n" +
            "    margin-top: 20px;\n" +
            "}\n" +
            ".scenario-link {\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "    padding: 12px 15px;\n" +
            "    background: #f8f9fa;\n" +
            "    border: 2px solid #e9ecef;\n" +
            "    border-radius: 8px;\n" +
            "    text-decoration: none;\n" +
            "    color: #333;\n" +
            "    transition: all 0.3s ease;\n" +
            "}\n" +
            ".scenario-link:hover {\n" +
            "    background: #e9ecef;\n" +
            "    border-color: #667eea;\n" +
            "    transform: translateX(5px);\n" +
            "    box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);\n" +
            "}\n" +
            ".scenario-number {\n" +
            "    display: inline-flex;\n" +
            "    align-items: center;\n" +
            "    justify-content: center;\n" +
            "    width: 30px;\n" +
            "    height: 30px;\n" +
            "    background: #667eea;\n" +
            "    color: white;\n" +
            "    border-radius: 50%;\n" +
            "    font-weight: bold;\n" +
            "    margin-right: 12px;\n" +
            "    font-size: 14px;\n" +
            "}\n" +
            ".scenario-name {\n" +
            "    flex: 1;\n" +
            "    font-weight: 500;\n" +
            "    margin-right: 10px;\n" +
            "}\n" +
            ".scenario-time {\n" +
            "    color: #999;\n" +
            "    font-size: 12px;\n" +
            "    white-space: nowrap;\n" +
            "}\n" +
            ".container {\n" +
            "    max-width: 1400px;\n" +
            "    margin: 0 auto;\n" +
            "    padding: 20px;\n" +
            "}\n" +
            ".scenario {\n" +
            "    background: white;\n" +
            "    margin-bottom: 20px;\n" +
            "    border-radius: 10px;\n" +
            "    overflow: hidden;\n" +
            "    box-shadow: 0 4px 6px rgba(0,0,0,0.1);\n" +
            "}\n" +
            ".scenario-header {\n" +
            "    padding: 20px;\n" +
            "    background: #f8f9fa;\n" +
            "    cursor: pointer;\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "    justify-content: space-between;\n" +
            "}\n" +
            ".scenario-header:hover {\n" +
            "    background: #e9ecef;\n" +
            "}\n" +
            ".toggle-icon {\n" +
            "    font-size: 20px;\n" +
            "    margin-right: 15px;\n" +
            "    transition: transform 0.3s;\n" +
            "}\n" +
            ".toggle-icon.collapsed {\n" +
            "    transform: rotate(-90deg);\n" +
            "}\n" +
            ".scenario-header h2 {\n" +
            "    flex-grow: 1;\n" +
            "    color: #333;\n" +
            "}\n" +
            ".scenario-meta {\n" +
            "    display: flex;\n" +
            "    gap: 15px;\n" +
            "    align-items: center;\n" +
            "}\n" +
            ".badge {\n" +
            "    background: #667eea;\n" +
            "    color: white;\n" +
            "    padding: 5px 12px;\n" +
            "    border-radius: 15px;\n" +
            "    font-size: 14px;\n" +
            "}\n" +
            ".scenario-content {\n" +
            "    padding: 20px;\n" +
            "    display: none;\n" +
            "}\n" +
            ".scenario-content.active {\n" +
            "    display: block;\n" +
            "}\n" +
            ".step-group {\n" +
            "    margin-bottom: 30px;\n" +
            "    padding: 20px;\n" +
            "    background: #f8f9fa;\n" +
            "    border-radius: 8px;\n" +
            "}\n" +
            ".step-group h3 {\n" +
            "    color: #495057;\n" +
            "    margin-bottom: 15px;\n" +
            "    font-size: 18px;\n" +
            "}\n" +
            ".screenshots-row {\n" +
            "    display: flex;\n" +
            "    gap: 20px;\n" +
            "    flex-wrap: wrap;\n" +
            "    justify-content: center;\n" +
            "}\n" +
            ".screenshot-card {\n" +
            "    position: relative;\n" +
            "    border: 2px solid #dee2e6;\n" +
            "    border-radius: 8px;\n" +
            "    overflow: hidden;\n" +
            "    background: white;\n" +
            "}\n" +
            ".screenshot-label {\n" +
            "    position: absolute;\n" +
            "    top: 10px;\n" +
            "    left: 10px;\n" +
            "    background: rgba(102, 126, 234, 0.9);\n" +
            "    color: white;\n" +
            "    padding: 5px 10px;\n" +
            "    border-radius: 5px;\n" +
            "    font-size: 12px;\n" +
            "    font-weight: bold;\n" +
            "    z-index: 1;\n" +
            "}\n" +
            ".screenshot-card img {\n" +
            "    width: 300px;\n" +
            "    height: auto;\n" +
            "    display: block;\n" +
            "    cursor: pointer;\n" +
            "    transition: transform 0.3s;\n" +
            "}\n" +
            ".screenshot-card:hover img {\n" +
            "    transform: scale(1.05);\n" +
            "}\n" +
            ".modal {\n" +
            "    display: none;\n" +
            "    position: fixed;\n" +
            "    z-index: 1000;\n" +
            "    left: 0;\n" +
            "    top: 0;\n" +
            "    width: 100%;\n" +
            "    height: 100%;\n" +
            "    overflow: auto;\n" +
            "    background-color: rgba(0,0,0,0.9);\n" +
            "}\n" +
            ".modal-content {\n" +
            "    margin: auto;\n" +
            "    display: block;\n" +
            "    max-width: 90%;\n" +
            "    max-height: 90%;\n" +
            "    margin-top: 50px;\n" +
            "}\n" +
            "#caption {\n" +
            "    margin: auto;\n" +
            "    display: block;\n" +
            "    width: 80%;\n" +
            "    max-width: 700px;\n" +
            "    text-align: center;\n" +
            "    color: #ccc;\n" +
            "    padding: 10px 0;\n" +
            "}\n" +
            ".close {\n" +
            "    position: absolute;\n" +
            "    top: 15px;\n" +
            "    right: 35px;\n" +
            "    color: #f1f1f1;\n" +
            "    font-size: 40px;\n" +
            "    font-weight: bold;\n" +
            "    cursor: pointer;\n" +
            "}\n" +
            ".close:hover {\n" +
            "    color: #bbb;\n" +
            "}\n" +
            "#goToTopBtn {\n" +
            "    display: none;\n" +
            "    position: fixed;\n" +
            "    bottom: 30px;\n" +
            "    right: 30px;\n" +
            "    z-index: 999;\n" +
            "    border: none;\n" +
            "    outline: none;\n" +
            "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
            "    color: white;\n" +
            "    cursor: pointer;\n" +
            "    padding: 12px 16px;\n" +
            "    border-radius: 50px;\n" +
            "    font-size: 14px;\n" +
            "    font-weight: bold;\n" +
            "    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);\n" +
            "    transition: all 0.3s ease;\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "    gap: 8px;\n" +
            "}\n" +
            "#goToTopBtn:hover {\n" +
            "    transform: translateY(-5px);\n" +
            "    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);\n" +
            "}\n" +
            "#goToTopBtn.show {\n" +
            "    display: flex;\n" +
            "    animation: slideIn 0.3s ease;\n" +
            "}\n" +
            "@keyframes slideIn {\n" +
            "    from {\n" +
            "        opacity: 0;\n" +
            "        transform: translateY(100px);\n" +
            "    }\n" +
            "    to {\n" +
            "        opacity: 1;\n" +
            "        transform: translateY(0);\n" +
            "    }\n" +
            "}\n" +
            ".side-nav {\n" +
            "    position: fixed;\n" +
            "    top: 50%;\n" +
            "    left: 20px;\n" +
            "    transform: translateY(-50%);\n" +
            "    z-index: 998;\n" +
            "    background: white;\n" +
            "    border-radius: 12px;\n" +
            "    box-shadow: 0 4px 15px rgba(0,0,0,0.1);\n" +
            "    transition: all 0.3s ease;\n" +
            "    max-width: 280px;\n" +
            "}\n" +
            ".side-nav.collapsed {\n" +
            "    width: auto;\n" +
            "}\n" +
            ".side-nav.collapsed .nav-content {\n" +
            "    display: none;\n" +
            "}\n" +
            ".toggle-nav {\n" +
            "    position: absolute;\n" +
            "    right: -15px;\n" +
            "    top: 10px;\n" +
            "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
            "    border: none;\n" +
            "    color: white;\n" +
            "    width: 36px;\n" +
            "    height: 36px;\n" +
            "    border-radius: 50%;\n" +
            "    cursor: pointer;\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "    justify-content: center;\n" +
            "    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);\n" +
            "    transition: all 0.3s ease;\n" +
            "}\n" +
            ".toggle-nav:hover {\n" +
            "    transform: scale(1.1) translateY(-2px);\n" +
            "    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);\n" +
            "}\n" +
            ".nav-content {\n" +
            "    padding: 20px;\n" +
            "    padding-right: 30px;\n" +
            "}\n" +
            ".nav-content h3 {\n" +
            "    color: #333;\n" +
            "    margin-bottom: 15px;\n" +
            "    font-size: 16px;\n" +
            "    border-bottom: 2px solid transparent;\n" +
            "    border-image: linear-gradient(90deg, #667eea 0%, #764ba2 100%);\n" +
            "    border-image-slice: 1;\n" +
            "    padding-bottom: 8px;\n" +
            "}\n" +
            ".nav-links {\n" +
            "    display: flex;\n" +
            "    flex-direction: column;\n" +
            "    gap: 8px;\n" +
            "}\n" +
            ".nav-link {\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "    gap: 10px;\n" +
            "    padding: 8px 12px;\n" +
            "    background: #f8f9fa;\n" +
            "    border-radius: 6px;\n" +
            "    text-decoration: none;\n" +
            "    color: #333;\n" +
            "    font-size: 13px;\n" +
            "    transition: all 0.2s ease;\n" +
            "}\n" +
            ".nav-link:hover {\n" +
            "    background: #e9ecef;\n" +
            "    transform: translateX(5px);\n" +
            "}\n" +
            ".nav-num {\n" +
            "    display: flex;\n" +
            "    align-items: center;\n" +
            "    justify-content: center;\n" +
            "    width: 24px;\n" +
            "    height: 24px;\n" +
            "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
            "    color: white;\n" +
            "    border-radius: 50%;\n" +
            "    font-size: 11px;\n" +
            "    font-weight: bold;\n" +
            "    flex-shrink: 0;\n" +
            "}\n" +
            ".nav-text {\n" +
            "    overflow: hidden;\n" +
            "    text-overflow: ellipsis;\n" +
            "    white-space: nowrap;\n" +
            "}\n" +
            ".nav-more {\n" +
            "    text-align: center;\n" +
            "    color: #999;\n" +
            "    font-size: 12px;\n" +
            "    margin-top: 10px;\n" +
            "    padding-top: 10px;\n" +
            "    border-top: 1px solid #e9ecef;\n" +
            "}\n" +
            "@media (max-width: 768px) {\n" +
            "    .side-nav {\n" +
            "        display: none;\n" +
            "    }\n" +
            "    #goToTopBtn {\n" +
            "        right: 20px;\n" +
            "        bottom: 20px;\n" +
            "        padding: 10px 14px;\n" +
            "    }\n" +
            "}";
    }

    private String getJavaScript() {
        return "function toggleScenario(index) {\n" +
            "    const content = document.getElementById('scenario-' + index);\n" +
            "    const toggle = document.getElementById('toggle-' + index);\n" +
            "\n" +
            "    if (content.classList.contains('active')) {\n" +
            "        content.classList.remove('active');\n" +
            "        toggle.classList.add('collapsed');\n" +
            "    } else {\n" +
            "        content.classList.add('active');\n" +
            "        toggle.classList.remove('collapsed');\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "function openModal(img, caption) {\n" +
            "    const modal = document.getElementById('imageModal');\n" +
            "    const modalImg = document.getElementById('modalImage');\n" +
            "    const captionText = document.getElementById('caption');\n" +
            "\n" +
            "    modal.style.display = 'block';\n" +
            "    modalImg.src = img.src;\n" +
            "    captionText.innerHTML = caption;\n" +
            "}\n" +
            "\n" +
            "function closeModal() {\n" +
            "    document.getElementById('imageModal').style.display = 'none';\n" +
            "}\n" +
            "\n" +
            "// Close modal on escape key\n" +
            "document.addEventListener('keydown', function(event) {\n" +
            "    if (event.key === 'Escape') {\n" +
            "        closeModal();\n" +
            "    }\n" +
            "});\n" +
            "\n" +
            "// Close modal on click outside\n" +
            "window.onclick = function(event) {\n" +
            "    const modal = document.getElementById('imageModal');\n" +
            "    if (event.target == modal) {\n" +
            "        closeModal();\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "// Function to expand and scroll to scenario\n" +
            "function expandAndScroll(index) {\n" +
            "    const scenario = document.getElementById('scenario-anchor-' + index);\n" +
            "    const content = document.getElementById('scenario-' + index);\n" +
            "    const toggle = document.getElementById('toggle-' + index);\n" +
            "    \n" +
            "    // Expand the scenario if it's collapsed\n" +
            "    if (!content.classList.contains('active')) {\n" +
            "        content.classList.add('active');\n" +
            "        toggle.classList.remove('collapsed');\n" +
            "    }\n" +
            "    \n" +
            "    // Smooth scroll to the scenario\n" +
            "    if (scenario) {\n" +
            "        scenario.scrollIntoView({ behavior: 'smooth', block: 'start' });\n" +
            "        // Add highlight effect\n" +
            "        scenario.style.animation = 'highlight 2s ease-out';\n" +
            "        setTimeout(() => {\n" +
            "            scenario.style.animation = '';\n" +
            "        }, 2000);\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "// Add highlight animation\n" +
            "const style = document.createElement('style');\n" +
            "style.textContent = `\n" +
            "    @keyframes highlight {\n" +
            "        0% { box-shadow: 0 0 0 0 rgba(102, 126, 234, 0.7); }\n" +
            "        50% { box-shadow: 0 0 20px 10px rgba(102, 126, 234, 0.3); }\n" +
            "        100% { box-shadow: 0 4px 6px rgba(0,0,0,0.1); }\n" +
            "    }\n" +
            "`;\n" +
            "document.head.appendChild(style);\n" +
            "\n" +
            "// Go to Top functionality\n" +
            "function goToTop() {\n" +
            "    window.scrollTo({ top: 0, behavior: 'smooth' });\n" +
            "}\n" +
            "\n" +
            "// Show/hide Go to Top button on scroll\n" +
            "window.onscroll = function() {\n" +
            "    const goToTopBtn = document.getElementById('goToTopBtn');\n" +
            "    if (document.body.scrollTop > 300 || document.documentElement.scrollTop > 300) {\n" +
            "        goToTopBtn.classList.add('show');\n" +
            "    } else {\n" +
            "        goToTopBtn.classList.remove('show');\n" +
            "    }\n" +
            "};\n" +
            "\n" +
            "// Toggle side navigation\n" +
            "let sideNavCollapsed = false;\n" +
            "function toggleSideNav() {\n" +
            "    const sideNav = document.getElementById('sideNav');\n" +
            "    sideNavCollapsed = !sideNavCollapsed;\n" +
            "    if (sideNavCollapsed) {\n" +
            "        sideNav.classList.add('collapsed');\n" +
            "    } else {\n" +
            "        sideNav.classList.remove('collapsed');\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "// Expand first scenario by default\n" +
            "document.addEventListener('DOMContentLoaded', function() {\n" +
            "    const firstContent = document.getElementById('scenario-0');\n" +
            "    if (firstContent) {\n" +
            "        firstContent.classList.add('active');\n" +
            "    }\n" +
            "    \n" +
            "    // Initialize side nav as collapsed on smaller screens\n" +
            "    if (window.innerWidth < 1400) {\n" +
            "        const sideNav = document.getElementById('sideNav');\n" +
            "        if (sideNav) {\n" +
            "            sideNav.classList.add('collapsed');\n" +
            "            sideNavCollapsed = true;\n" +
            "        }\n" +
            "    }\n" +
            "});";
    }

    // Helper classes
    static class TestScenario {
        String name;
        String timestamp;
        List<Screenshot> screenshots = new ArrayList<>();
    }

    static class Screenshot {
        String fileName;
        String path;
        String type;
        int stepNumber;
    }
}