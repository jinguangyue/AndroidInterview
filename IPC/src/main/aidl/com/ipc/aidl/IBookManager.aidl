// IBookManager.aidl
package com.ipc.aidl;

import com.ipc.aidl.Book;
import com.ipc.aidl.IOnNewBookArrivedListner;
// Declare any non-default types here with import statements

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);
    void register(IOnNewBookArrivedListner iOnNewBookArrivedListner);
    void unregister(IOnNewBookArrivedListner iOnNewBookArrivedListner);
}
