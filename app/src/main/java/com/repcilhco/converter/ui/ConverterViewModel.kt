package com.repcilhco.converter.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ConverterViewModel : ViewModel() {
	
	var total by mutableStateOf(0.0)
		private set
	
	var iva by mutableStateOf(0.0)
		private set
	
	var isr by mutableStateOf(0.0)
		private set
	
	var totalTaxes by mutableStateOf(0.0)
		private set
	
	fun calculate(normalIva: Boolean, totalToTax: Boolean) {
		
		when {
			normalIva && totalToTax -> {
				iva = total * 0.16
				totalTaxes = (total + iva)
			}
			
			normalIva && !totalToTax -> {
				setTotalValue((totalTaxes * 100.0) / 116.0)
				iva = total * 0.16
			}
			
			!normalIva && totalToTax -> {
				iva = total * 0.16
				isr = total * 0.0125
				totalTaxes = (total + iva - isr)
			}
			
			else -> {
				isr = (totalTaxes / 100) * 1.08933
				iva = (totalTaxes / 100) * 13.94335
				total = (totalTaxes - (iva - isr))
				
			}
		}
	}
	
	fun setTotalValue(double: Double) {
		total = double
	}
	
	fun setTotalTaxesValue(double: Double) {
		totalTaxes = double
	}
}