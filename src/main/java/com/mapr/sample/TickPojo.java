package com.mapr.sample;

public class TickPojo {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSymbolroot() {
        return symbolroot;
    }

    public void setSymbolroot(String symbolroot) {
        this.symbolroot = symbolroot;
    }

    public String getSymbolsuffix() {
        return symbolsuffix;
    }

    public void setSymbolsuffix(String symbolsuffix) {
        this.symbolsuffix = symbolsuffix;
    }

    public String getTradeVolume() {
        return tradeVolume;
    }

    public String getSaleCondition() {
        return saleCondition;
    }

    public void setSaleCondition(String saleCondition) {
        this.saleCondition = saleCondition;
    }

    public void setTradeVolume(String tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getTradeStopStockIndicator() {
        return tradeStopStockIndicator;
    }

    public void setTradeStopStockIndicator(String tradeStopStockIndicator) {
        this.tradeStopStockIndicator = tradeStopStockIndicator;
    }

    public String getTradeCorrectionIndicator() {
        return tradeCorrectionIndicator;
    }

    public void setTradeCorrectionIndicator(String tradeCorrectionIndicator) {
        this.tradeCorrectionIndicator = tradeCorrectionIndicator;
    }

    public String getTradeSequenceNumber() {
        return tradeSequenceNumber;
    }

    public void setTradeSequenceNumber(String tradeSequenceNumber) {
        this.tradeSequenceNumber = tradeSequenceNumber;
    }

    public String getTradeSource() {
        return tradeSource;
    }

    public void setTradeSource(String tradeSource) {
        this.tradeSource = tradeSource;
    }

    public String getTradeReportingFacility() {
        return tradeReportingFacility;
    }

    public void setTradeReportingFacility(String tradeReportingFacility) {
        this.tradeReportingFacility = tradeReportingFacility;
    }

    String date;
    String exchange;
    String symbolroot;
    String symbolsuffix;
    String saleCondition;
    String tradeVolume;
    String tradePrice;
    String tradeStopStockIndicator;
    String tradeCorrectionIndicator;
    String tradeSequenceNumber;
    String tradeSource;
    String tradeReportingFacility;
    String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    String[] receivers;

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }
}
