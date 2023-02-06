package com.repcilhco.converter.domain

interface IVAUseCase {
	
	fun getIva(isTotal:Boolean):Double
}


internal class IVAUseCaseInt:IVAUseCase {
	
	override fun getIva(isTotal: Boolean): Double {
		return 0.16.takeIf { !isTotal } ?: 13.94335
	}
}