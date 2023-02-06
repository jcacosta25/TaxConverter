package com.repcilhco.converter.domain

interface ISRUseCase {
	fun invoke(isTotal: Boolean): Double
}

internal class ISRUseCaseInt : ISRUseCase {
	override fun invoke(isTotal: Boolean): Double {
		return 0.0125.takeIf { !isTotal } ?: 1.0893
	}
}