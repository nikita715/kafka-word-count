package com.nikstep.kafkawordcount.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "words")
data class WordCount(
        @Id
        val id: Long,
        @Column(nullable = false)
        val word: String,
        @Column(nullable = false)
        val count: Int
)