// IOnNewBookArrivedListner.aidl
package com.ipc.aidl;

// Declare any non-default types here with import statements
import com.ipc.aidl.Book;
interface IOnNewBookArrivedListner {
    void onBookArrived(in Book newBook);
}
