package com.ipc.binderpool;

import android.os.RemoteException;

public class computeImpl extends IComput.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
