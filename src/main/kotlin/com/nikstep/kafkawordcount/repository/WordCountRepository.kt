package com.nikstep.kafkawordcount.repository

import com.nikstep.kafkawordcount.model.WordCount
import org.springframework.data.jpa.repository.JpaRepository

interface WordCountRepository : JpaRepository<WordCount, Long>