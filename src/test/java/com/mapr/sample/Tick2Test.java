package com.mapr.sample;

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

public class Tick2Test {

    public static final double N = 1e7;

    @org.junit.Test
    public void testGetDate() throws IOException {
        List<String> data = Resources.readLines(Resources.getResource("sample-tick-01.txt"), Charsets.ISO_8859_1);
        Tick t = new Tick(data.get(0));
        assertEquals(t.getDate(), "080845201");

        ObjectMapper mapper = new ObjectMapper();
        System.out.printf("%s\n", mapper.writeValueAsString(t));
    }

    @Test
    public void testSpeed() throws Exception {
        List<String> data = Resources.readLines(Resources.getResource("sample-tick-01.txt"), Charsets.ISO_8859_1);
        ObjectMapper mapper = new ObjectMapper();

        File tempFile = File.createTempFile("foo", "data");
        tempFile.deleteOnExit();
        System.out.printf("file = %s\n", tempFile);
        byte[] NEWLINE = "\n".getBytes();
        double t0 = System.nanoTime() * 1e-9;
        int m = data.size();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile), 10_000)) {
            for (int i = 0; i < N; i++) {
                int j = i % m;
                Tick t = new Tick(data.get(j));
                out.write(mapper.writeValueAsBytes(t));
//                out.write(NEWLINE);
            }
        }
        long size = tempFile.length();
        double t = System.nanoTime() * 1e-9 - t0;
        System.out.printf("t = %.3f us, %.2f records/s, %.2f MB/s\n", t / N * 1e6, N / t, size / t / 1e6);
    }

    @Test
    public void testBinarySpeed() throws Exception {
        List<String> data = Resources.readLines(Resources.getResource("sample-tick-01.txt"), Charsets.ISO_8859_1);

        double t0 = System.nanoTime() * 1e-9;
        File tempFile = File.createTempFile("foo", "data");
        tempFile.deleteOnExit();
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile), 10_000))) {
            for (int i = 0; i < N; i++) {
                int j = i % data.size();
                Tick t = new Tick(data.get(j));
                out.writeObject(t);
            }
        }
        double t = System.nanoTime() * 1e-9 - t0;
        System.out.printf("t = %.3f us, %.2f records/s\n", t / N * 1e6, N / t);
    }

    // how fast can we go from string to JSON to string?
    @Test
    public void testJsonSpeed() throws Exception {
//        File file = new File(Resources.getResource("sample-tick-01.txt").getFile());
//        FileReader fr = new FileReader(file);
//        BufferedReader reader = new BufferedReader(fr);
//        String line = reader.readLine();
//        List<byte[]> data = new LinkedList<byte[]>();
//        try {
//            while (line != null) {
//                data.add(line.getBytes(Charsets.ISO_8859_1));
//                line = reader.readLine();
//            }
//        } catch (Exception e) {
//            System.err.println("ERROR: " + e);
//        }

        List<String> data = Resources.readLines(Resources.getResource("sample-tick-01.txt"), Charsets.ISO_8859_1);

        double t0 = System.nanoTime() * 1e-9;
        File tempFile = File.createTempFile("foo", "data");
        tempFile.deleteOnExit();
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile), 10_000))) {
            for (int i = 0; i < N; i++) {
                int j = i % data.size();
                JSONObject tick = parse_json(data.get(j));
                out.writeObject(tick);
            }
        }
        double t = System.nanoTime() * 1e-9 - t0;
        System.out.printf("t = %.3f us, %.2f records/s\n", t / N * 1e6, N / t);
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
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile), 10_000))) {
            for (int i = 0; i < N; i++) {
                int j = i % data.size();
                TickPojo tick = parse_pojo(data.get(j));
                out.writeObject(tick);
            }
        }
        double t = System.nanoTime() * 1e-9 - t0;
        System.out.printf("t = %.3f us, %.2f records/s\n", t / N * 1e6, N / t);
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

//    @Test
//    public void testByteSpeed() throws Exception {
//
//    }
}