package com.prashik.scorer.util;

import android.content.Intent;

import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchPlayer;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Over;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.models.Team;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

    public static void syncNameToIdMapData(String fileName, HashMap<String, String> hashMap) {
        ObjectOutputStream objectOutputStream = writeFile(fileName);
        try {
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.printf("Error while writing %s file.%n", fileName);
            throw new RuntimeException(e);
        }
    }

    public static void syncMatchData(String fileName, Match match) {
        ObjectOutputStream objectOutputStream = writeFile(fileName);
        try {
            objectOutputStream.writeObject(match);
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

    public static HashMap<String, String> readNameToIdMapFile(String fileName) {
        HashMap<String, String> hashMap;
        ObjectInputStream objectInputStream = readFile(fileName);
        try {
            hashMap = (HashMap<String, String>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return hashMap;
    }

    public static Match readMatchFile(String fileName) {
        Match match;
        ObjectInputStream objectInputStream = readFile(fileName);
        try {
            match = (Match) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw  new RuntimeException(e);
        }
        return match;
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

    public static boolean isEven(int val) {
        int ans = val % 2;
        return ans == 0;
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

    public static HashMap<String, String> getPlayerNamesToIdMap(HashMap<String, Player> allPlayers) {
        HashMap<String, String> nameToIdMap = new HashMap<>();
        for(String key: allPlayers.keySet()) {
            String firstName = allPlayers.get(key).getFirstName();
            String lastName = allPlayers.get(key).getLastName();
            String name = firstName + " " + lastName;
            String id = allPlayers.get(key).getId();
            nameToIdMap.put(name, id);
        }
        return nameToIdMap;
    }

    public static String[] getPlayersList(HashMap<String, String> allPlayers) {
        ArrayList<String> players = new ArrayList<>();
        for(String key: allPlayers.keySet()) {
            players.add(key);
        }
        return players.toArray(new String[0]);
    }

    public static MatchPlayer getMatchPlayer(String name,
                                             HashMap<String, String> map,
                                             HashMap<String, Player> allPlayers) {
        String id = map.get(name);
        Player player = allPlayers.get(id);
        MatchPlayer matchPlayer = new MatchPlayer(player);
        assert player != null;
        return matchPlayer;
    }


    public static String getScore(Team team) {
        return team.getRuns() +
                "/" +
                team.getWickets();
    }

    public static String getOvers(Team team, Over over, Match match) {
        if(over == null) {
            System.out.println("Didn't receive over object. I will assume that the team didn't even bowl.");
            return "0.0/" + match.getMaxOvers();
        }

        String balls = "0";
        if(over.getLegalDeliveries() != 6) {
            balls = Integer.toString(over.getLegalDeliveries());
        }
        return team.getCurrentOverBowling() +
                "." +
                balls +
                "/" +
                match.getMaxOvers();
    }

    public static String getOverDetails(Over over) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String str: over.getOverSummary()) {
            stringBuilder.append(str);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static ArrayList<String> getAllFilesInDirectory(String directory) {
        ArrayList<String> filesList = new ArrayList<>();
        File folder = new File(directory);
        for(File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if(fileEntry.isDirectory()) {
                continue;
            }
            if(fileEntry.isFile()) {
                filesList.add(fileEntry.getName());
            }
        }
        return filesList;
    }

    public static ArrayList<String> getMatchFiles(ArrayList<String> filesList) {
        ArrayList<String> temp = new ArrayList<>();
        for(String str: filesList) {
            if(str.startsWith("match")) {
                temp.add(str);
            }
        }
        return temp;
    }

    public static boolean isMatchAlreadyExists(String directory, Match match) {
        ArrayList<String> matchFiles = getMatchFiles(getAllFilesInDirectory(directory));
        System.out.println("Match Files: " + matchFiles);
        for(String matchfile: matchFiles) {
            String fileToRead = directory + "/" + matchfile;
            System.out.println("File to read: " + fileToRead);
            Match previousMatch = readMatchFile(fileToRead);
            if(previousMatch.isEqual(match)) {
                System.out.println("Match - " + previousMatch.getId() + " matches with new match.");
                System.out.println("Previous Match: " + previousMatch);
                System.out.println("New Match: " + match);
                return true;
            }
        }
        return false;
    }

    public static boolean equateArrayList(ArrayList<String> arr1, ArrayList<String> arr2) {
        boolean result = true;
        if(arr1.size() != arr2.size()) {
            return false;
        }

        for(int i=0; i<arr1.size(); i++) {
            if(arr1.get(i).equals(arr2.get(i))) {
                return false;
            }
        }

        return result;
    }
}
