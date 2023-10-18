package io.github.viabachelora23michaelkutaibakasper.bprapp.data.repository

import io.github.viabachelora23michaelkutaibakasper.bprapp.data.domain.Event

interface IEventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun getEvent(code: String): Event?
}