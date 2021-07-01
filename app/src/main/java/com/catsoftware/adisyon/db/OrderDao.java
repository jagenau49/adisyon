package com.catsoftware.adisyon.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM `ordersTable` WHERE isDeleted=:isDeleted ORDER BY ID DESC")
    List<Order> getOrders(boolean isDeleted);//return all undeleted orders and the newest is top

    @Query("SELECT * FROM `ordersTable` WHERE ID=:orderID ")
    List<Order> getDetailsOfOrder(int orderID); //returns details of specified order

    @Query("SELECT * FROM `ordersTable` WHERE driver=:driver AND isDeleted=:isDeleted ")
    List<Order> getAllOrdersOfDriver(String driver, boolean isDeleted); //returns all undeleted orders of driver


    @Query("UPDATE `ordersTable` SET hour=:hour, minute=:minute, driver=:driver, " +
            "paymentMethod=:paymentMethod, price=:price, orderNo=:orderNo " +
            "WHERE ID=:idEditingOrder")
    void updateOrder(int idEditingOrder, int hour, int minute, String driver,
                     String paymentMethod, double price, String orderNo);
    //updates specified order with new values


    @Query("UPDATE `ordersTable` SET isDeleted=:isDeleted WHERE ID=:ID")
    void setIsDeleted(int ID, boolean isDeleted);//updates selected order as deleted

    @Query("SELECT COUNT(ID) FROM `ordersTable` " +
            "WHERE  registrationYear!=:registrationYear OR registrationMonth!=:registrationMonth OR  registrationDay!=:registrationDay")
    int getCountOldOrders(int registrationYear, int registrationMonth, int registrationDay);
    //returns count of old orders

    @Query("DELETE FROM `ordersTable` WHERE registrationYear!=:registrationYear")
    void deleteOldYear(int registrationYear);//deletes old years orders

    @Query("DELETE FROM `ordersTable` WHERE registrationMonth!=:registrationMonth")
    void deleteOldMonth(int registrationMonth);//deletes old months orders

    @Query("DELETE FROM `ordersTable` WHERE registrationDay!=:registrationDay")
    void deleteOldDay(int registrationDay);//deletes old days orders




    @Insert
    void insertOrder(Order... ordersTable);


}