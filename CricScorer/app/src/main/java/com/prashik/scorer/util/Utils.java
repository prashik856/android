package com.prashik.scorer.util;

import android.content.Intent;
import android.util.Log;

import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchPlayer;
import com.prashik.scorer.models.MatchPlayerBatting;
import com.prashik.scorer.models.MatchPlayerBowling;
import com.prashik.scorer.models.MatchPlayerFielding;
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
import java.util.Arrays;
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
        System.out.println("All players hashmap: " + allPlayers.toString());
        ArrayList<String> players = new ArrayList<>(allPlayers.keySet());
        String[] returnArray = players.toArray(new String[0]);
        Arrays.sort(returnArray);
        return returnArray;
    }

    public static String[] getRemainingPlayersList(HashMap<String, String> allPlayers, Match match) {
        String[] allPlayersString = getPlayersList(allPlayers);
        System.out.println("All Players List: " + Arrays.toString(allPlayersString));
        ArrayList<String> matchPlayers = match.getMatchPlayers();
        System.out.println("Match Players List: " + matchPlayers);
        ArrayList<String> temp = new ArrayList<>();
        for (String player : allPlayersString) {
            if (!matchPlayers.contains(player)) {
                temp.add(player);
            }
        }
        String[] returnArray = temp.toArray(new String[0]);
        Arrays.sort(returnArray);
        return returnArray;
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

    public static String getOvers(Team team, Match match) {
        int overs = team.getLegalDeliveriesPlayed() / 6;
        int balls = team.getLegalDeliveriesPlayed() % 6;
        return overs + "." + balls + "/" + match.getMaxOvers();
    }

    public static String getOverDetails(Over over) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String str: over.getOverSummary()) {
            stringBuilder.append(str);
            stringBuilder.append("  ");
        }
        stringBuilder.append("| This Over - ").append(over.getRuns());
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

    public static void updateGlobalRecords(HashMap<String, String> dataFilesMap, Match match) {
        HashMap<String, BattingStats> allBattingStats = new HashMap<>();
        HashMap<String, BowlingStats> allBowlingStats = new HashMap<>();
        HashMap<String, MatchStats> allMatchesStats = new HashMap<>();
        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            switch (s) {
                case "players_batting_data_file_location":
                    allBattingStats = Utils.readBattingStatsFile(dataFile);
                    break;
                case "players_bowling_data_file_location":
                    allBowlingStats = Utils.readBowlingStatsFile(dataFile);
                    break;
                case "players_matches_data_file_location":
                    allMatchesStats = Utils.readMatchStatsFile(dataFile);
                    break;
            }
        }



        // how will it work for common player -> update record twice.
        for(MatchPlayer matchPlayer: match.getTeamA().getTeamPlayers()) {

            // an extra iteration for the common player
            if(matchPlayer.getPlayerName().equals(match.getTeamA().getCommonName())) {
                // update batting records
                updateGlobalBattingRecords(allBattingStats, matchPlayer, match.getId(), true);

                // update bowling records
                updateGlobalBowlingRecords(allBowlingStats, matchPlayer, match.getId(), true);

                // update match records
                updateGlobalMatchRecords(allMatchesStats, matchPlayer, match.getId(), true);
                continue;
            }

            // update batting records
            updateGlobalBattingRecords(allBattingStats, matchPlayer, match.getId(), false);

            // update bowling records
            updateGlobalBowlingRecords(allBowlingStats, matchPlayer, match.getId(), false);

            // update match records
            updateGlobalMatchRecords(allMatchesStats, matchPlayer, match.getId(), false);
        }

        for(MatchPlayer matchPlayer: match.getTeamB().getTeamPlayers()) {
            // update batting records
            updateGlobalBattingRecords(allBattingStats, matchPlayer, match.getId(), false);

            // update bowling records
            updateGlobalBowlingRecords(allBowlingStats, matchPlayer, match.getId(), false);

            // update match records
            updateGlobalMatchRecords(allMatchesStats, matchPlayer, match.getId(), false);
        }

        for(String s: dataFilesMap.keySet()) {
            String dataFile = dataFilesMap.get(s);
            Log.d("debug", String.format("Data file location: key - %s, location - %s", s, dataFile));
            switch (s) {
                case "players_batting_data_file_location":
                    Utils.syncBattingStatsData(dataFile, allBattingStats);
                    break;
                case "players_bowling_data_file_location":
                    Utils.syncBowlingStatsData(dataFile, allBowlingStats);
                    break;
                case "players_matches_data_file_location":
                    Utils.syncMatchStatsData(dataFile, allMatchesStats);
                    break;
            }
        }
    }

    public static void updateGlobalMatchRecords(HashMap<String, MatchStats> allMatchStats,
                                                MatchPlayer matchPlayer, String matchId, boolean commonPlayer) {
        MatchStats matchStats = allMatchStats.get(matchPlayer.getPlayer().getId());

        assert matchStats != null;
        if(matchStats.getMatchesIncluded().containsKey(matchId)) {
            System.out.println("Match data is already included in Global Bowling Stats.");
            return;
        }

        if(!commonPlayer) {
            matchStats.getMatchesIncluded().put(matchId, true);
        }
        MatchPlayerFielding matchPlayerFielding = matchPlayer.getMatchPlayerFielding();

        if(!commonPlayer) {
            matchStats.setMatchesPlayed(
                    matchStats.getMatchesPlayed() + 1
            );
        }

        matchStats.setCatches(
                matchStats.getCatches() + matchPlayerFielding.getNoOfCatches()
        );

        matchStats.setRunOuts(
                matchStats.getRunOuts() + matchPlayerFielding.getNoOfRunOuts()
        );
    }

    public static void updateGlobalBowlingRecords(HashMap<String, BowlingStats> allBowlingStats,
                                                  MatchPlayer matchPlayer, String matchId, boolean commonPlayer) {
        BowlingStats bowlingStats = allBowlingStats.get(matchPlayer.getPlayer().getId());

        assert bowlingStats != null;
        if(bowlingStats.getMatchesIncluded().containsKey(matchId)) {
            System.out.println("Match data is already included in Global Bowling Stats.");
            return;
        }

        if(!commonPlayer) {
            bowlingStats.getMatchesIncluded().put(matchId, true);
        }
        MatchPlayerBowling matchPlayerBowling = matchPlayer.getMatchPlayerBowling();

        if(matchPlayerBowling.isBowled()) {
            bowlingStats.setInningsBowled(
                    bowlingStats.getInningsBowled() + 1
            );

            bowlingStats.setWickets(
                    bowlingStats.getWickets() + matchPlayerBowling.getWicketsTaken()
            );

            if(matchPlayerBowling.isTwoFer()) {
                bowlingStats.setTwoFer(
                        bowlingStats.getTwoFer() + 1
                );
            }

            if(matchPlayerBowling.isThreeFer()) {
                bowlingStats.setThreeFer(
                        bowlingStats.getThreeFer() + 1
                );
            }

            if(matchPlayerBowling.isFiveFer()) {
                bowlingStats.setFiveFer(
                        bowlingStats.getFiveFer() + 1
                );
            }

            bowlingStats.setFours(
                    bowlingStats.getFours() + matchPlayerBowling.getFoursConceded()
            );

            bowlingStats.setSixes(
                    bowlingStats.getSixes() + matchPlayerBowling.getSixesConceded()
            );

            bowlingStats.setDots(
                    bowlingStats.getDots() + matchPlayerBowling.getDotsConceded()
            );

            bowlingStats.setWides(
                    bowlingStats.getWides() + matchPlayerBowling.getWideBalls()
            );

            bowlingStats.setNos(
                    bowlingStats.getNos() + matchPlayerBowling.getNoBalls()
            );

            bowlingStats.setNumberOfOvers(
                    bowlingStats.getNumberOfOvers() + matchPlayerBowling.getNoOfOvers()
            );

            bowlingStats.setExtras(
                    bowlingStats.getExtras() + matchPlayerBowling.getExtrasConceded()
            );

            bowlingStats.setRuns(
                    bowlingStats.getRuns() + matchPlayerBowling.getRunsConceded()
            );

            bowlingStats.setDeliveriesBowled(
                    bowlingStats.getDeliveriesBowled() + matchPlayerBowling.getDeliveriesBowled()
            );

            bowlingStats.setLegalDeliveriesBowled(
                    bowlingStats.getLegalDeliveriesBowled() + matchPlayerBowling.getLegalDeliveriesBowled()
            );

            bowlingStats.setMaidensBowled(
                    bowlingStats.getMaidensBowled() + matchPlayerBowling.getMaidenOverBowled()
            );

            bowlingStats.setBowledWickets(
                    bowlingStats.getBowledWickets() + matchPlayerBowling.getBowledPlayers().size()
            );

            double newEconomy = (bowlingStats.getEconomy() + matchPlayerBowling.getEconomy())/bowlingStats.getInningsBowled();
            bowlingStats.setEconomy(newEconomy);

            double newAverage = 0;
            if(bowlingStats.getWickets() > 0) {
                newAverage = (bowlingStats.getAverage() + matchPlayerBowling.getRunsConceded())/bowlingStats.getWickets();
            }
            bowlingStats.setAverage(newAverage);

            String currentBest = bowlingStats.getBestBowling();
            int wickets = Integer.parseInt(currentBest.split("-")[0]);
            int runs = Integer.parseInt(currentBest.split("-")[1]);

            String newBest = matchPlayerBowling.getWicketsTaken() + "-" + matchPlayerBowling.getRunsConceded();
            if(currentBest.equals("0-0")
                    || (matchPlayerBowling.getWicketsTaken() > wickets)
                    || (matchPlayerBowling.getWicketsTaken() >= wickets && matchPlayerBowling.getRunsConceded() < runs)) {
                bowlingStats.setBestBowling(newBest);
            }

        }
    }

    public static void updateGlobalBattingRecords(HashMap<String, BattingStats> allBattingStats,
                                                  MatchPlayer matchPlayer, String matchId, boolean commonPlayer) {
        BattingStats battingStats = allBattingStats.get(matchPlayer.getPlayer().getId());
        assert battingStats != null;

        if(battingStats.getMatchesIncluded().containsKey(matchId)) {
            System.out.println("Match data is already included in Global Batting Stats.");
            return;
        }

        if(!commonPlayer) {
            // put match id
            battingStats.getMatchesIncluded().put(matchId, true);
        }
        MatchPlayerBatting matchPlayerBatting = matchPlayer.getMatchPlayerBatting();

        if(matchPlayerBatting.isBatted()) {
            battingStats.setInningsPlayed(
                    battingStats.getInningsPlayed() + 1
            );

            battingStats.setRuns(
                    battingStats.getRuns() + matchPlayerBatting.getRunsScored()
            );

            battingStats.setFours(
                    battingStats.getFours() + matchPlayerBatting.getFoursScored()
            );

            battingStats.setSixes(
                    battingStats.getSixes() + matchPlayerBatting.getSixesScored()
            );

            battingStats.setDots(
                    battingStats.getDots() + matchPlayerBatting.getDotsPlayed()
            );

            battingStats.setBallsPlayed(
                    battingStats.getBallsPlayed() + matchPlayerBatting.getBallsPlayed()
            );

            if(matchPlayerBatting.isOut()) {
                battingStats.setOutCount(
                        battingStats.getOutCount() + 1
                );
            }

            if (matchPlayerBatting.isTwenty()) {
                battingStats.setTwenties(
                        battingStats.getTwenties() + 1
                );
            }

            if (matchPlayerBatting.isThirty()) {
                battingStats.setThirties(
                        battingStats.getThirties() + 1
                );
            }

            if (matchPlayerBatting.isFifty()) {
                battingStats.setFifties(
                        battingStats.getFifties() + 1
                );
            }

            double runningAverage = 0;
            if(battingStats.getOutCount() <= 0) {
                runningAverage = battingStats.getRuns();
            } else {
                runningAverage = (double) battingStats.getRuns() /battingStats.getOutCount();
            }
            battingStats.setBattingAverage(runningAverage);

            double runningStrikeRate = 0;
            if(battingStats.getBallsPlayed() > 0) {
                runningStrikeRate = ((double)battingStats.getRuns()/battingStats.getBallsPlayed()) * 100;
            }
            battingStats.setStrikeRate(runningStrikeRate);

            if(matchPlayerBatting.getRunsScored() > battingStats.getBestScore()) {
                battingStats.setBestScore(matchPlayerBatting.getRunsScored());
            }
        }
    }

    public static void syncNameToIdMapping(HashMap<String, Player> allPlayers, HashMap<String, String> nameToIdMap,
                                           HashMap<String, String> dataFilesMap) {
        for(String playerId: allPlayers.keySet()) {
            String playerName = Objects.requireNonNull(allPlayers.get(playerId)).getFullName();

            if(nameToIdMap.get(playerName) == null) {
                nameToIdMap.put(playerName, playerId);
            } else {
                System.out.println("Error syncning player " + playerName + ". Data already exists in naming map.");
            }
        }
        String fileName = dataFilesMap.get("players_name_to_id_map_file_location");
        Utils.syncNameToIdMapData(fileName, nameToIdMap);
    }

    public static MatchPlayer getMatchPlayersFromList(ArrayList<MatchPlayer> removedPlayers,
                                                      String playerName,
                                                      HashMap<String, String> nameToIdMap,
                                                      HashMap<String, Player> allPlayersMap) {
        MatchPlayer matchPlayer = null;
        for(int i=0; i<removedPlayers.size(); i++) {
            if(removedPlayers.get(i).getPlayerName().equals(playerName)) {
                System.out.println("MatchPlayer object already exists. Returning that object.");
                matchPlayer = removedPlayers.get(i);
                removedPlayers.remove(i);
                break;
            }
        }

        if(matchPlayer == null) {
            System.out.println("MatchPlayer Object doesn't exists in the existing teams. Returning new object.");
            String playerId = nameToIdMap.get(playerName);
            Player player = allPlayersMap.get(playerId);
            matchPlayer = new MatchPlayer(player);
        }

        return matchPlayer;
    }
}
