package com.company;

import org.web3j.crypto.RawTransaction;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class Transaction {
    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private BigInteger value;
    private String data;
    private BigInteger txtype;


    private Transaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data) {
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.to = to;
        this.value = value;
        if (data != null) {
            this.data = Numeric.cleanHexPrefix(data);
        }
        int f = 1;
        BigInteger d = BigInteger.valueOf(f);
        this.txtype = d;

    }

    public static Transaction createTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data) {
        return new Transaction(nonce, gasPrice, gasLimit, to, value, data);
    }

    public BigInteger getNonce() {
        return this.nonce;
    }

    public BigInteger getGasPrice() {
        return this.gasPrice;
    }

    public BigInteger getGasLimit() {
        return this.gasLimit;
    }

    public String getTo() {
        return this.to;
    }

    public BigInteger getValue() {
        return this.value;
    }

    public String getData() {
        return this.data;
    }

    public BigInteger getTxtype() {
        return this.txtype;
    }

}
