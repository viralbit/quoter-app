package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM saved_quote_cards ORDER BY createdAt DESC")
    fun getAllSavedQuotes(): Flow<List<SavedQuoteEntity>>

    @Query("SELECT * FROM saved_quote_cards WHERE id = :id LIMIT 1")
    suspend fun getQuoteById(id: Long): SavedQuoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: SavedQuoteEntity): Long

    @Query("DELETE FROM saved_quote_cards WHERE id = :id")
    suspend fun deleteQuoteById(id: Long)

    @Query("SELECT COUNT(*) FROM saved_quote_cards")
    fun getSavedQuoteCount(): Flow<Int>
}
