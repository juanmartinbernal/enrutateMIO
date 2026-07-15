package com.enrutatemio.domain.recharge

import com.enrutatemio.core.model.RechargePoint

class GetRechargePointsUseCase(private val repository: RechargeRepository) {
    suspend operator fun invoke(query: String = ""): List<RechargePoint> =
        if (query.isBlank()) repository.getAllRechargePoints() else repository.searchRechargePoints(query)
}
