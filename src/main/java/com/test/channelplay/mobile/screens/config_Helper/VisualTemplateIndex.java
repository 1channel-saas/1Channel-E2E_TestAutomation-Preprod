package com.test.channelplay.mobile.screens.config_Helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VisualTemplateIndex {

    private static final Logger log = LoggerFactory.getLogger(VisualTemplateIndex.class);
    private static final String INDEX_FILE = "templates/visual_index/global_index.json";
    private final int maxVersionsPerElement;
    private final long maxFolderSizeMB;
    private final int maxVersionFolders;

    private Map<String, TemplateEntry> hashToTemplate = new ConcurrentHashMap<>();
    private Map<String, List<TemplateEntry>> elementNameToTemplates = new ConcurrentHashMap<>();
    private Map<String, FolderStats> folderStats = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public VisualTemplateIndex() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Load configuration
        this.maxVersionsPerElement = TemplateConfig.getMaxVersionsPerElement();
        this.maxFolderSizeMB = TemplateConfig.getMaxFolderSizeMB();
        this.maxVersionFolders = TemplateConfig.getMaxVersionFolders();

        loadIndex();
    }

    /**
     * Add a new template to the index with visual hash
     */
    public void addTemplate(String templatePath, String elementName, BufferedImage image) {
        try {
            String hash = generateImageHash(image);
            TemplateEntry entry = new TemplateEntry(templatePath, hash, elementName, new Date());

            // Add to hash index
            hashToTemplate.put(hash, entry);

            // Add to element name index with version management
            manageElementVersions(elementName, entry);

            // Update folder stats
            updateFolderStats(templatePath);

            // Check if folder rotation needed
            checkAndRotateFolders();

            saveIndex();
            log.info("Added template to visual index: {} with hash: {}", templatePath, hash);

        } catch (Exception e) {
            log.error("Failed to add template to visual index: {}", e.getMessage());
        }
    }

    /**
     * Find template by visual similarity (hash matching)
     */
    public String findTemplateByVisualMatch(BufferedImage currentImage) {
        return findTemplateByVisualMatch(currentImage, null);
    }

    /**
     * Find template by visual similarity with element name filtering
     */
    public String findTemplateByVisualMatch(BufferedImage currentImage, String elementName) {
        try {
            String currentHash = generateImageHash(currentImage);

            // First try exact match with element name filtering
            if (elementName != null) {
                for (Map.Entry<String, TemplateEntry> entry : hashToTemplate.entrySet()) {
                    if (entry.getKey().equals(currentHash) &&
                        entry.getValue().elementName != null &&
                        entry.getValue().elementName.toLowerCase().contains(elementName.toLowerCase()) &&
                        Files.exists(Paths.get(entry.getValue().path))) {
                        log.info("Found exact visual match for element '{}': {}",
                                 elementName, entry.getValue().path);
                        return entry.getValue().path;
                    }
                }
            } else {
                // No element name filter - use original exact match
                TemplateEntry match = hashToTemplate.get(currentHash);
                if (match != null && Files.exists(Paths.get(match.path))) {
                    log.info("Found visual match for template: {}", match.path);
                    return match.path;
                }
            }

            // Try fuzzy matching with tolerance and element name filtering
            return findSimilarTemplate(currentHash, elementName);

        } catch (Exception e) {
            log.error("Failed to find visual match: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Find templates by element name across all version folders
     */
    public List<String> findTemplatesByName(String elementName) {
        String sanitizedName = sanitizeElementName(elementName);
        List<String> templates = new ArrayList<>();

        // Search in current folder first
        templates.addAll(searchInFolder("templates/screens/current", sanitizedName));
        templates.addAll(searchInFolder("templates/AI_images/current", sanitizedName));

        // Search in version folders (newest first)
        templates.addAll(searchInVersionFolders("templates/screens", sanitizedName));
        templates.addAll(searchInVersionFolders("templates/AI_images", sanitizedName));

        return templates.stream()
                .filter(path -> Files.exists(Paths.get(path)))
                .collect(Collectors.toList());
    }

    /**
     * Manage versions per element (max 3)
     */
    private void manageElementVersions(String elementName, TemplateEntry newEntry) {
        // Skip versioning for manual templates
        if (newEntry.path.contains("manual_captured_images")) {
            List<TemplateEntry> versions = elementNameToTemplates.computeIfAbsent(
                elementName, k -> new ArrayList<>()
            );
            versions.add(0, newEntry); // Add without version management
            return;
        }

        List<TemplateEntry> versions = elementNameToTemplates.computeIfAbsent(
            elementName, k -> new ArrayList<>()
        );

        versions.add(0, newEntry); // Add new version at beginning

        // Keep only maxVersionsPerElement versions (but NEVER delete manual templates)
        if (versions.size() > maxVersionsPerElement) {
            List<TemplateEntry> toRemove = new ArrayList<>();
            int autoTemplateCount = 0;

            // Count from newest to oldest, removing only auto-captured templates
            for (TemplateEntry entry : versions) {
                // Skip manual templates from deletion
                if (entry.path.contains("manual_captured_images")) {
                    continue;
                }

                autoTemplateCount++;
                if (autoTemplateCount > maxVersionsPerElement) {
                    toRemove.add(entry);
                }
            }

            for (TemplateEntry entry : toRemove) {
                try {
                    Files.deleteIfExists(Paths.get(entry.path));
                    hashToTemplate.remove(entry.hash);
                    log.info("Deleted old auto-captured version: {}", entry.path);
                } catch (IOException e) {
                    log.warn("Failed to delete old version: {}", entry.path);
                }
            }

            versions.removeAll(toRemove);
        }
    }

    /**
     * Check folder size and rotate if needed
     */
    private void checkAndRotateFolders() {
        checkAndRotateFolder("templates/screens");
        checkAndRotateFolder("templates/AI_images");
    }

    private void checkAndRotateFolder(String basePath) {
        try {
            Path currentPath = Paths.get(basePath, "current");
            if (!Files.exists(currentPath)) {
                Files.createDirectories(currentPath);
                return;
            }

            long folderSizeMB = getFolderSizeMB(currentPath);

            if (folderSizeMB > maxFolderSizeMB) {
                // Create version folder with date
                String dateStr = new java.text.SimpleDateFormat("ddMMMyyyy").format(new Date());
                String versionName = "version_" + dateStr;
                Path versionPath = Paths.get(basePath, versionName);

                // Rename current to version folder
                Files.move(currentPath, versionPath);
                Files.createDirectories(currentPath);

                log.info("Rotated folder: {} -> {}", currentPath, versionPath);

                // Clean up old version folders if > MAX_VERSION_FOLDERS
                cleanupOldVersionFolders(basePath);
            }

        } catch (IOException e) {
            log.error("Failed to rotate folder: {}", e.getMessage());
        }
    }

    private void cleanupOldVersionFolders(String basePath) {
        try {
            List<Path> versionFolders = Files.list(Paths.get(basePath))
                    .filter(Files::isDirectory)
                    .filter(p -> p.getFileName().toString().startsWith("version_"))
                    .sorted((p1, p2) -> {
                        try {
                            return Files.getLastModifiedTime(p2).compareTo(
                                   Files.getLastModifiedTime(p1));
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .collect(Collectors.toList());

            if (versionFolders.size() > maxVersionFolders) {
                List<Path> toDelete = versionFolders.subList(
                    maxVersionFolders, versionFolders.size()
                );

                for (Path folder : toDelete) {
                    deleteFolder(folder);
                    log.info("Deleted old version folder: {}", folder);
                }
            }

        } catch (IOException e) {
            log.error("Failed to cleanup old folders: {}", e.getMessage());
        }
    }

    private void deleteFolder(Path folder) throws IOException {
        Files.walk(folder)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        log.warn("Failed to delete: {}", path);
                    }
                });
    }

    private long getFolderSizeMB(Path folder) throws IOException {
        long bytes = Files.walk(folder)
                .filter(Files::isRegularFile)
                .mapToLong(path -> {
                    try {
                        return Files.size(path);
                    } catch (IOException e) {
                        return 0;
                    }
                }).sum();

        return bytes / (1024 * 1024); // Convert to MB
    }

    /**
     * Generate perceptual hash for image
     */
    private String generateImageHash(BufferedImage image) throws NoSuchAlgorithmException {
        // Resize image to 8x8 for perceptual hashing
        BufferedImage resized = resizeImage(image, 8, 8);

        // Convert to grayscale and generate hash
        StringBuilder hashBuilder = new StringBuilder();
        int[][] grayscale = new int[8][8];
        int total = 0;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int rgb = resized.getRGB(x, y);
                int gray = (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF)) / 3;
                grayscale[y][x] = gray;
                total += gray;
            }
        }

        int average = total / 64;

        // Generate hash based on whether each pixel is above average
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                hashBuilder.append(grayscale[y][x] > average ? "1" : "0");
            }
        }

        // Convert binary string to hex
        return binaryToHex(hashBuilder.toString());
    }

    private BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = resized.createGraphics();
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }

    private String binaryToHex(String binary) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 4) {
            String chunk = binary.substring(i, Math.min(i + 4, binary.length()));
            hex.append(Integer.toHexString(Integer.parseInt(chunk, 2)));
        }
        return hex.toString();
    }

    /**
     * Find similar template with hamming distance
     */
    private String findSimilarTemplate(String targetHash) {
        return findSimilarTemplate(targetHash, null);
    }

    /**
     * Find similar template with hamming distance and element name filtering
     */
    private String findSimilarTemplate(String targetHash, String elementName) {
        int minDistance = Integer.MAX_VALUE;
        TemplateEntry bestMatch = null;

        for (Map.Entry<String, TemplateEntry> entry : hashToTemplate.entrySet()) {
            TemplateEntry candidate = entry.getValue();

            // Filter by element name if provided
            if (elementName != null && !elementName.isEmpty()) {
                // Check if the template's element name contains the search element name
                if (candidate.elementName == null ||
                    !candidate.elementName.toLowerCase().contains(elementName.toLowerCase())) {
                    continue; // Skip this template as it doesn't match the element name
                }
            }

            int distance = hammingDistance(targetHash, entry.getKey());
            if (distance < minDistance && distance <= 2) { // Stricter threshold - reduced from 5 to <=2
                minDistance = distance;
                bestMatch = candidate;
            }
        }

        if (bestMatch != null && Files.exists(Paths.get(bestMatch.path))) {
            log.info("Found similar template with distance {} for element '{}': {}",
                     minDistance, elementName != null ? elementName : "any", bestMatch.path);
            return bestMatch.path;
        }

        return null;
    }

    private int hammingDistance(String hash1, String hash2) {
        int distance = 0;
        int minLen = Math.min(hash1.length(), hash2.length());

        for (int i = 0; i < minLen; i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                distance++;
            }
        }

        distance += Math.abs(hash1.length() - hash2.length());
        return distance;
    }

    private List<String> searchInFolder(String folderPath, String elementName) {
        List<String> results = new ArrayList<>();
        try {
            Path folder = Paths.get(folderPath);
            if (Files.exists(folder)) {
                Files.list(folder)
                        .filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().startsWith(elementName))
                        .forEach(p -> results.add(p.toString()));
            }
        } catch (IOException e) {
            log.debug("Error searching folder {}: {}", folderPath, e.getMessage());
        }
        return results;
    }

    private List<String> searchInVersionFolders(String basePath, String elementName) {
        List<String> results = new ArrayList<>();
        try {
            Path base = Paths.get(basePath);
            if (Files.exists(base)) {
                Files.list(base)
                        .filter(Files::isDirectory)
                        .filter(p -> p.getFileName().toString().startsWith("version_"))
                        .sorted((p1, p2) -> p2.getFileName().compareTo(p1.getFileName()))
                        .forEach(versionFolder -> {
                            results.addAll(searchInFolder(versionFolder.toString(), elementName));
                        });
            }
        } catch (IOException e) {
            log.debug("Error searching version folders in {}: {}", basePath, e.getMessage());
        }
        return results;
    }

    private void updateFolderStats(String templatePath) {
        try {
            Path path = Paths.get(templatePath);
            String folderPath = path.getParent().toString();

            FolderStats stats = folderStats.computeIfAbsent(
                folderPath, k -> new FolderStats()
            );

            stats.updateSize(getFolderSizeMB(path.getParent()));
            stats.lastModified = new Date();

        } catch (IOException e) {
            log.debug("Failed to update folder stats: {}", e.getMessage());
        }
    }

    private String sanitizeElementName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();
    }

    private void loadIndex() {
        try {
            Path indexPath = Paths.get(INDEX_FILE);
            if (Files.exists(indexPath)) {
                IndexData data = objectMapper.readValue(indexPath.toFile(), IndexData.class);

                // Rebuild maps from loaded data
                for (TemplateEntry entry : data.templates) {
                    hashToTemplate.put(entry.hash, entry);
                    elementNameToTemplates.computeIfAbsent(
                        entry.elementName, k -> new ArrayList<>()
                    ).add(entry);
                }

                folderStats = new ConcurrentHashMap<>(data.folderStats);
                log.info("Loaded visual index with {} templates", hashToTemplate.size());
            }
        } catch (Exception e) {
            log.warn("Could not load visual index, starting fresh: {}", e.getMessage());
        }
    }

    private void saveIndex() {
        try {
            Path indexPath = Paths.get(INDEX_FILE);
            Files.createDirectories(indexPath.getParent());

            IndexData data = new IndexData();
            data.templates = new ArrayList<>(hashToTemplate.values());
            data.folderStats = folderStats;

            objectMapper.writeValue(indexPath.toFile(), data);
            log.debug("Saved visual index with {} templates", data.templates.size());

        } catch (Exception e) {
            log.error("Failed to save visual index: {}", e.getMessage());
        }
    }

    // Data classes for JSON serialization
    static class TemplateEntry {
        public String path;
        public String hash;
        public String elementName;
        public Date created;
        public int version;

        public TemplateEntry() {}

        public TemplateEntry(String path, String hash, String elementName, Date created) {
            this.path = path;
            this.hash = hash;
            this.elementName = elementName;
            this.created = created;
            this.version = 1;
        }
    }

    static class FolderStats {
        public long sizeMB;
        public Date created;
        public Date lastModified;

        public FolderStats() {
            this.created = new Date();
            this.lastModified = new Date();
        }

        public void updateSize(long sizeMB) {
            this.sizeMB = sizeMB;
        }
    }

    static class IndexData {
        public List<TemplateEntry> templates = new ArrayList<>();
        public Map<String, FolderStats> folderStats = new HashMap<>();
    }
}