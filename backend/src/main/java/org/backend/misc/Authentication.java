package org.backend.misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.backend.user.UserCred;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Authentication {
    public static ArrayList<UserCred> allUsers = new ArrayList<>();

    private static final String filePath = "src/main/java/org/backend/user/data/userCredDB.json";

    public Authentication() {
        readUserCredFromJson();
    }

    public int login(String username, String password) {
        if (allUsers == null) {
            System.err.println("User list is null, initializing...");
            allUsers = new ArrayList<>();
        }

        // Find the user by username
        UserCred userCredentials = allUsers.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElse(null);

        if (userCredentials == null) {
            return -1;
        }

        // Check if the hashed password matches
        if (!Util.hashPassword(password).equals(userCredentials.getHashedPassword())) {
            return -1;
        }

        System.err.println(STR."User \{username} logged in successfully");

        // Return the user ID
        return userCredentials.getId();
    }

    public static void readUserCredFromJson() {
        try {
            // Load the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Iterate over the array of user objects
            Iterator<JsonNode> elements = rootNode.elements();
            while (elements.hasNext()) {
                JsonNode userNode = elements.next();

                int userId = userNode.get("userId").asInt();
                String username = userNode.get("username").asText();
                String hashedPassword = userNode.get("hashedPassword").asText();

                // Create a new UserCred object and add it to the list
                UserCred user = new UserCred(userId, username, hashedPassword);
                allUsers.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProfilePicture(String username) {
        try {
            // Load the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Iterate over the array of user objects
            Iterator<JsonNode> elements = rootNode.elements();
            while (elements.hasNext()) {
                JsonNode userNode = elements.next();

                String usernameJson = userNode.get("username").asText();
                String profilePicture = userNode.get("profilePicture").asText();

                if (username.equals(usernameJson)) {
                    return profilePicture;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}