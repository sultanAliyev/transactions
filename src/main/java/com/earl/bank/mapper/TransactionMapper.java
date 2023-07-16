package com.earl.bank.mapper;

import com.earl.bank.entity.Transaction;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO transactions(sender_id, receiver_id, amount, currency, description) " +
            "VALUES(#{senderId},#{receiverId},#{amount},#{currency},#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createTransaction(Transaction transaction);

    @Select("SELECT * FROM transactions WHERE id=#{transactionId}")
    Transaction getTransaction(@Param("transactionId") Long transactionId);
}
