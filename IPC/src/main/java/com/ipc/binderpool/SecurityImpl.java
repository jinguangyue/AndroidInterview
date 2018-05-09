package com.ipc.binderpool;

import android.os.RemoteException;

import com.ipc.binderpool.ISecurityCenter.Stub;

public class SecurityImpl extends Stub {

    private static final char SCRETE_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for(int i=0; i<chars.length; i++){
            chars[i] ^= SCRETE_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
