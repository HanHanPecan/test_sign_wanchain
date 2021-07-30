package com.company;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.tx.ChainId;
import org.web3j.utils.Numeric;
import org.web3j.abi.Utils;
import org.web3j.crypto.*;

import java.util.ArrayList;
import java.util.List;

import org.web3j.crypto.Sign.SignatureData;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import java.util.ArrayList;


public class Main {


    public static Credentials exportPrivateKey(String keystorePath, String password) throws IOException, CipherException {
        Credentials credentials = WalletUtils.loadCredentials(password, keystorePath);
        //  BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
        return credentials;
    }

    public static byte[] encode(Transaction rawTransaction, byte chainId) {
        SignatureData signatureData = new SignatureData(chainId, new byte[0], new byte[0]);
        return encode(rawTransaction, signatureData);
    }


    private static byte[] encode(Transaction rawTransaction, SignatureData signatureData) {
        List<RlpType> values = asRlpValues(rawTransaction, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    static List<RlpType> asRlpValues(Transaction rawTransaction, SignatureData signatureData) {
        List<RlpType> result = new ArrayList();
        result.add(RlpString.create(rawTransaction.getTxtype()));
        result.add(RlpString.create(rawTransaction.getNonce()));
        result.add(RlpString.create(rawTransaction.getGasPrice()));
        result.add(RlpString.create(rawTransaction.getGasLimit()));
        String to = rawTransaction.getTo();
        if (to != null && to.length() > 0) {
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(rawTransaction.getValue()));
        byte[] data = Numeric.hexStringToByteArray(rawTransaction.getData());
        result.add(RlpString.create(data));
        if (signatureData != null) {
            result.add(RlpString.create(signatureData.getV()));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        return result;
    }

    public static SignatureData createEip155SignatureData(SignatureData signatureData, byte chainId) {
        byte v = (byte) (signatureData.getV() + (chainId << 1) + 8);
        return new SignatureData(v, signatureData.getR(), signatureData.getS());
    }

    public static void main(String[] args) throws IOException, CipherException {
        // write your code here
        int a = 500000000;
        int b = 21000;
        int f = 100000000;


        BigInteger d = BigInteger.valueOf(a);
        BigInteger c = BigInteger.valueOf(b);
        BigInteger e = BigInteger.valueOf(f);


        String to = "0x18d3fc395e7c69d227d29147540c42bc4141ef71";
        Transaction rawTransaction = Transaction.createTransaction(BigInteger.ZERO, d, c, to, e, "0x");
        byte MAINNET = (byte) 1;
//        String prk = "8ce91e582b506e8cf67af04bd1f2e27248a7b44a8cddb7213dd9fc599c08bb7e";
//        String pub = "0x29c405bd06a5b8bbe0b9c78ea2eecf3c4310a831";
        String path = "/Users/nancy/go/src/github.com/wanchain/go-wanchain/build/bin/data/keystore/UTC--2021-07-30T03-51-25.514882000Z--29c405Bd06a5B8bbE0B9c78Ea2EecF3c4310A831";
        Credentials credentials = exportPrivateKey(path, "");
        byte[] encodedTransaction = encode(rawTransaction, ChainId.MAINNET);
        SignatureData signatureData = Sign.signMessage(encodedTransaction, credentials.getEcKeyPair());
        SignatureData eip155SignatureData = createEip155SignatureData(signatureData, ChainId.MAINNET);
        byte[] dd = encode(rawTransaction, eip155SignatureData);

        String hexss = Numeric.toHexString(dd);
        System.out.println(hexss);

    }
}
