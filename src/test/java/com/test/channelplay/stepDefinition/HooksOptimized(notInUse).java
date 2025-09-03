package com.test.channelplay.stepDefinition;

/**
 * Optimized Hooks for faster test execution between scenarios
 * Reuses browser instance when possible, only performs full teardown when necessary
 */
/*public class HooksOptimized extends DriverBase {

    private static final Logger logger = LoggerFactory.getLogger(HooksOptimized.class);
    private static WebDriver driver;
    private static boolean isFirstScenario = true;
    private static int scenarioCount = 0;
    private static final int MAX_SCENARIOS_BEFORE_RESTART = 10;  // Restart browser after N scenarios to prevent memory issues

    @BeforeAll
    public static void setupClass() {
        // One-time setup before all scenarios
        String browser = System.getProperty("browser", "chrome");
        logger.info("Setting up WebDriverManager for {}", browser);
        
        if (browser.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
        } else if (browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
        }
        isFirstScenario = true;
    }

    @Before
    public void setupScenario(Scenario scenario) {
        scenarioCount++;
        
        // Initialize driver only if it's null or we've exceeded max scenarios
        if (driver == null || scenarioCount > MAX_SCENARIOS_BEFORE_RESTART) {
            if (driver != null) {
                driver.quit();
                driver = null;
                scenarioCount = 1;
            }
            
            // Only clear cache on first run or after restart
            if (isFirstScenario || scenarioCount == 1) {
                String browser = System.getProperty("browser", "chrome");
                if (isFirstScenario) {
                    clearCacheForBrowser(browser);
                    isFirstScenario = false;
                }
            }
            
            driver = initialize(System.getProperty("browser", "chrome"));
            logger.info("New driver initialized for scenario: {}", scenario.getName());
        } else {
            // Reuse existing driver, just clear session
            try {
                driver.manage().deleteAllCookies();
                driver.get("about:blank");
                logger.info("Reusing driver for scenario: {}", scenario.getName());
            } catch (Exception e) {
                logger.warn("Failed to clear session, creating new driver: {}", e.getMessage());
                driver.quit();
                driver = initialize(System.getProperty("browser", "chrome"));
            }
        }
    }

    @After(order = 1)
    public void captureFailure(Scenario scenario) {
        if (scenario.isFailed() && driver != null && driver instanceof TakesScreenshot) {
            addScreenshot(scenario);
        }
        if (driver != null) {
            addPageLink(scenario);
        }
    }

    @After(order = 0)
    public void cleanupScenario(Scenario scenario) {
        // Only perform minimal cleanup between scenarios
        // Full teardown happens in @AfterAll
        if (driver != null) {
            try {
                // Navigate to blank page to clear JavaScript state
                driver.get("about:blank");
                // Clear local storage and session storage
                driver.manage().deleteAllCookies();
            } catch (Exception e) {
                logger.warn("Failed to cleanup scenario: {}", e.getMessage());
            }
        }
    }

    @AfterAll
    public static void teardownClass() {
        // Full cleanup after all scenarios
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully after all scenarios");
            } catch (Exception e) {
                logger.error("Failed to quit driver: {}", e.getMessage());
            } finally {
                driver = null;
                // Kill any remaining driver processes
                killDriverProcess();
            }
        }
    }

    private static void killDriverProcess() {
        String os = System.getProperty("os.name").toLowerCase();
        String command;
        
        if (os.contains("win")) {
            command = "taskkill /F /IM chromedriver.exe /T";
        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
            command = "pkill -f chromedriver";
        } else {
            return;
        }
        
        try {
            Runtime.getRuntime().exec(command);
            Thread.sleep(500);  // Brief pause to ensure process is killed
        } catch (Exception e) {
            logger.debug("Could not kill driver process: {}", e.getMessage());
        }
    }

    private void addPageLink(Scenario scenario) {
        try {
            scenario.log(String.format("Test page: %s", driver.getCurrentUrl()));
            scenario.log(String.format("Test Browser: %s", System.getProperty("browser")));
        } catch (Exception e) {
            logger.debug("Could not add page link: {}", e.getMessage());
        }
    }

    private void addScreenshot(Scenario scenario) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot");
        } catch (Exception e) {
            logger.debug("Could not capture screenshot: {}", e.getMessage());
        }
    }

    public void clearCacheForBrowser(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().clearDriverCache();
                logger.info("Cleared ChromeDriver cache");
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().clearDriverCache();
                logger.info("Cleared GeckoDriver cache");
                break;
            default:
                logger.info("No cache clearing for browser: {}", browserName);
        }
    }
}*/