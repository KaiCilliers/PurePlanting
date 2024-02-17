package com.sunrisekcdeveloper.pureplanting.core.alarm

import com.sunrisekcdeveloper.pureplanting.core.alarm.AlarmInfo.Companion.toDomain
import com.sunrisekcdeveloper.pureplanting.core.database.alarm.AlarmInfoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AlarmInfoRepository {
    suspend fun add(alarmInfo: AlarmInfo)
    suspend fun remove(alarmInfo: AlarmInfo)
    suspend fun all(): List<AlarmInfo>

    class Default(
        private val dao: AlarmInfoDao,
        private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : AlarmInfoRepository {

        override suspend fun add(alarmInfo: AlarmInfo) {
            withContext(dispatcher) {
                dao.insert(alarmInfo.toEntity())
            }
        }

        override suspend fun remove(alarmInfo: AlarmInfo) {
            withContext(dispatcher) {
                dao.delete(alarmInfo.time, alarmInfo.type.name)
            }
        }

        override suspend fun all(): List<AlarmInfo> {
            return withContext(dispatcher) {
                dao.allAlarms().map { it.toDomain() }
            }
        }

    }
}