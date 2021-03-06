package com.mapr.sample;


/* DESCRIPTION:
 * This JUnit tests compares how fast we can serialize data objects of various
 * formats. We simulate Kafka serialization by reading string data form a
 * file, casting it to a data record of a specific type (e.g. POJO,
 * JsonObject, or JSON annotated Byte Array), then writing the object back out
 * to a file. Essentially, we're simulating Kafka streams as file streams and
 * measuring how long it takes to convert a string data record to a Java object
 * that encapsulates the record's data fields.
 *
 * USAGE:
 * mvn -e -Dtest=TypeFormatSpeedTest test
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.*;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class TypeFormatSpeedTest {

    public static final double N = 1e6;

    @Test
    public void testJsonSpeed() throws Exception {

        List<String> data = Resources.readLines(Resources.getResource("sample-tick-01.txt"), Charsets.ISO_8859_1);

        double t0 = System.nanoTime() * 1e-9;
        File tempFile = File.createTempFile("foo", "data");
        tempFile.deleteOnExit();
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile), 10_000_00))) {
            for (int i = 0; i < N; i++) {
                int j = i % data.size();
                JSONObject tick = parse_json(data.get(j));
                out.writeObject(tick);
            }
        }
        double t = System.nanoTime() * 1e-9 - t0;
        System.out.printf("[testJsonSpeed] t = %.3f us, %.2f records/s\n", t / N * 1e6, N / t);
    }

    private static JSONObject parse_json(String record) throws ParseException {
        // TODO: handle corrupted messages or messages with missing fields gracefully
        if (record.length() < 71) {
            throw new ParseException("Expected line to be at least 71 characters, but got " + record.length(), record.length());
        }

        JSONObject trade_info = new JSONObject();
        trade_info.put("date", record.substring(0, 9));
        trade_info.put("exchange", record.substring(9, 10));
        trade_info.put("symbol root", record.substring(10, 16).trim());
        trade_info.put("symbol suffix", record.substring(16, 26).trim());
        trade_info.put("saleCondition", record.substring(26, 30).trim());
        trade_info.put("tradeVolume", record.substring(30, 39));
        trade_info.put("tradePrice", record.substring(39, 46) + "." + record.substring(46, 50));
        trade_info.put("tradeStopStockIndicator", record.substring(50, 51));
        trade_info.put("tradeCorrectionIndicator", record.substring(51, 53));
        trade_info.put("tradeSequenceNumber", record.substring(53, 69));
        trade_info.put("tradeSource", record.substring(69, 70));
        trade_info.put("tradeReportingFacility", record.substring(70, 71));
        if (record.length() >= 74) {
            trade_info.put("sender", record.substring(71, 75));

            JSONArray receiver_list = new JSONArray();
            int i = 0;
            while (record.length() >= 78 + i) {
                receiver_list.add(record.substring(75 + i, 79 + i));
                i += 4;
            }
            trade_info.put("receivers", receiver_list);
        }
        return trade_info;
    }

    @Test
    public void testPojoSpeed() throws Exception {
        List<String> data = Resources.readLines(Resources.getResource("sample-tick-01.txt"), Charsets.ISO_8859_1);

        double t0 = System.nanoTime() * 1e-9;
        File tempFile = File.createTempFile("foo", "data");
        tempFile.deleteOnExit();
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile), 10_000_00))) {
            for (int i = 0; i < N; i++) {
                int j = i % data.size();
                TickPojo tick = parse_pojo(data.get(j));
                out.writeObject(tick);
            }
        }
        double t = System.nanoTime() * 1e-9 - t0;
        System.out.printf("[testPojoSpeed] t = %.3f us, %.2f records/s\n", t / N * 1e6, N / t);
    }

    private static TickPojo parse_pojo(String record) throws ParseException {
        // TODO: handle corrupted messages or messages with missing fields gracefully
        if (record.length() < 71) {
            throw new ParseException("Expected line to be at least 71 characters, but got " + record.length(), record.length());
        }

        TickPojo trade_info = new TickPojo();
        trade_info.setDate(record.substring(0, 9));
        trade_info.setExchange(record.substring(9, 10));
        trade_info.setSymbolroot(record.substring(10, 16).trim());
        trade_info.setSymbolsuffix(record.substring(16, 26).trim());
        trade_info.setSaleCondition(record.substring(26, 30).trim());
        trade_info.setTradeVolume(record.substring(30, 39));
        trade_info.setTradePrice(record.substring(39, 46) + "." + record.substring(46, 50));
        trade_info.setTradeStopStockIndicator(record.substring(50, 51));
        trade_info.setTradeCorrectionIndicator(record.substring(51, 53));
        trade_info.setTradeSequenceNumber(record.substring(53, 69));
        trade_info.setTradeSource(record.substring(69, 70));
        trade_info.setTradeReportingFacility(record.substring(70, 71));
        if (record.length() >= 74) {
            trade_info.setSender(record.substring(71, 75));

            List<String> receiver_list = new LinkedList<String>();
            int i = 0;
            while (record.length() >= 78 + i) {
                receiver_list.add(record.substring(75 + i, 79 + i));
                i += 4;
            }
            trade_info.setReceivers(receiver_list.toArray(new String[receiver_list.size()]));
        }
        return trade_info;
    }

    @Test
    public void testByteSpeed() throws Exception {
        List<String> data = Resources.readLines(Resources.getResource("sample-tick-01.txt"), Charsets.ISO_8859_1);

        double t0 = System.nanoTime() * 1e-9;
        File tempFile = File.createTempFile("foo", "data");
        tempFile.deleteOnExit();
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile), 10_000_00))) {
            for (int i = 0; i < N; i++) {
                int j = i % data.size();
                Tick tick = new Tick(data.get(j));
                out.writeObject(tick);
            }
        }
        double t = System.nanoTime() * 1e-9 - t0;
        System.out.printf("[testByteSpeed] t = %.3f us, %.2f records/s\n", t / N * 1e6, N / t);
    }
}