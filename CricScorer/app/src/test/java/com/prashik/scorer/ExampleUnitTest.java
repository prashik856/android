package com.prashik.scorer;

import org.junit.Test;

import static org.junit.Assert.*;

import com.prashik.scorer.models.BattingStats;
import com.prashik.scorer.models.BowlingStats;
import com.prashik.scorer.models.MatchStats;
import com.prashik.scorer.models.Player;
import com.prashik.scorer.util.Utils;

import java.util.Collections;
import java.util.HashMap;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void read_and_write_objects() {
        String testFileName = "test.bin";
        Utils.deleteFile(testFileName);
        Utils.createFile(testFileName);

        HashMap<String, Player> hashMap = new HashMap<>();
        Player player = new Player("p", "r", "e", "9");

        String id = player.getId();
        hashMap.put(player.getId(), player);

        Utils.syncPlayersData(testFileName, hashMap);

        HashMap<String, Player> newHashMap = Utils.readPlayersFile(testFileName);
        Player newPlayer = newHashMap.get(id);

        assertNotNull(newPlayer);
        assertTrue(player.isEqual(newPlayer));

        Utils.deleteFile(testFileName);
    }
}