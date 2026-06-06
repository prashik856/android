package com.prashik.scorer.util;

import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Objects;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;


public class Utils {

    public static ObjectOutputStream writeFile(String fileName) {
        ObjectOutputStream objectOutputStream;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found.%n", fileName);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.printf("Error while writing %s file.%n", fileName);
            throw new RuntimeException(e);
        }
        return objectOutputStream;
    }

    public static void syncPlayersData(String fileName, HashMap<String, Player> hashMap) {
        ObjectOutputStream objectOutputStream = writeFile(fileName);
        try {
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.printf("Error while writing %s file.%n", fileName);
            throw new RuntimeException(e);
        }
    }

    public static void syncBattingStatsData(String fileName, HashMap<String, BattingStats> hashMap) {
        ObjectOutputStream objectOutputStream = writeFile(fileName);
        try {
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.printf("Error while writing %s file.%n", fileName);
            throw new RuntimeException(e);
        }
    }

    public static void syncBowlingStatsData(String fileName, HashMap<String, BowlingStats> hashMap) {
        ObjectOutputStream objectOutputStream = writeFile(fileName);
        try {
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.printf("Error while writing %s file.%n", fileName);
            throw new RuntimeException(e);
        }
    }

    public static void syncMatchStatsData(String fileName, HashMap<String, MatchStats> hashMap) {
        ObjectOutputStream objectOutputStream = writeFile(fileName);
        try {
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.printf("Error while writing %s file.%n", fileName);
            throw new RuntimeException(e);
        }
    }

    public static ObjectInputStream readFile(String fileName) {
        ObjectInputStream objectInputStream;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            objectInputStream = new ObjectInputStream(fileInputStream);

        } catch (IOException e) {
            System.out.printf("File %s not found.%n", fileName);
            throw new RuntimeException(e);
        }
        return objectInputStream;
    }

    public static HashMap<String, Player> readPlayersFile(String fileName) {
        HashMap<String, Player> hashMap;
        ObjectInputStream objectInputStream = readFile(fileName);
        try {
            hashMap = (HashMap<String, Player>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return hashMap;
    }

    public static HashMap<String, BattingStats> readBattingStatsFile(String fileName) {
        HashMap<String, BattingStats> hashMap;
        ObjectInputStream objectInputStream = readFile(fileName);
        try {
            hashMap = (HashMap<String, BattingStats>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return hashMap;
    }

    public static HashMap<String, BowlingStats> readBowlingStatsFile(String fileName) {
        HashMap<String, BowlingStats> hashMap;
        ObjectInputStream objectInputStream = readFile(fileName);
        try {
            hashMap = (HashMap<String, BowlingStats>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return hashMap;
    }

    public static HashMap<String, MatchStats> readMatchStatsFile(String fileName) {
        HashMap<String, MatchStats> hashMap;
        ObjectInputStream objectInputStream = readFile(fileName);
        try {
            hashMap = (HashMap<String, MatchStats>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return hashMap;
    }

    public static boolean createFile(String fileName) {
        boolean fileCreated = false;
        try {
            File file = new File(fileName);
            if(file.createNewFile()) {
                System.out.printf("File %s created%n", fileName);
                fileCreated = true;
            } else {
                System.out.printf("File %s already exists%n", fileName);
            }
        } catch (IOException e) {
            System.out.printf("Error while creating %s%n", fileName);
            throw new RuntimeException(e);
        }
        return fileCreated;
    }


    public static void deleteFile(String fileName) {
        try {
            File file = new File(fileName);
            boolean fileDeleted = file.delete();
            if (fileDeleted) {
                System.out.printf("File %s deleted.%n", fileName);
            } else {
                System.out.println("Either file does not or error deleting this file.");
            }

        } catch (RuntimeException e) {
            System.out.printf("Error deleting %s file%n", fileName);
        }
    }

    public static boolean playerAlreadyExists(HashMap<String, Player> hashMap, Player player) {
        boolean found = false;
        for(String key: hashMap.keySet()) {
            Player currentPlayer = hashMap.get(key);

            assert currentPlayer != null;
            if (player.isPlayerSame(currentPlayer)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static boolean playerAlreadyExistsEdit(HashMap<String, Player> hashMap, Player player) {
        boolean found = false;
        for(String key: hashMap.keySet()) {
            if (Objects.equals(key, player.getId())) {
                continue;
            }
            Player currentPlayer = hashMap.get(key);

            assert currentPlayer != null;
            if (player.isPlayerSame(currentPlayer)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static Intent putDataFiles(Intent intent,
                                      HashMap<String, String> dataFilesMap,
                                      HashMap<String, Player> allPlayers,
                                      HashMap<String, BattingStats> allBattingStats,
                                      HashMap<String, BowlingStats> allBowlingStats,
                                      HashMap<String, MatchStats> allMatchesStats) {
        intent.putExtra("data_files_hashmap", dataFilesMap);
        intent.putExtra("all_players_hashmap", allPlayers);
        intent.putExtra("all_batting_stats_hashmap", allBattingStats);
        intent.putExtra("all_bowling_stats_hashmap", allBowlingStats);
        intent.putExtra("all_matches_stats_hashmap", allMatchesStats);
        return intent;
    }


}
