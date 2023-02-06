package com.repcilhco.converter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.repcilhco.converter.ui.theme.ConverterTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ConverterTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					GreetingPreview()
				}
			}
		}
	}
}

@Composable
private fun SwitchWithText(name: String, change: Boolean, onCheckedChange: (Boolean) -> Unit) {
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Switch(
			checked = change,
			onCheckedChange = onCheckedChange
		)
		Text(text = name)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxesCalculator(
	normalIva: Boolean,
	total: Double,
	iva: Double,
	ivaPercent: Double,
	isr: Double,
	isrPercent: Double,
	totalTaxes: Double,
	isTotalToTax: Boolean,
	totalChange: (Double) -> Unit,
	totalTaxesChange: (Double) -> Unit
) {
	
	val df = DecimalFormat("#.##")
	df.roundingMode = RoundingMode.DOWN
	
	var totalText by remember(total) { mutableStateOf(df.format(total)) }
	var totalTaxesText by remember(totalTaxes) { mutableStateOf(df.format(totalTaxes)) }
	val ivaText by remember(iva) { mutableStateOf(df.format(iva)) }
	val isrText by remember(isr) { mutableStateOf(df.format(isr)) }
	
	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Text(text = "Total")
		TextField(
			readOnly = !isTotalToTax,
			value = totalText,
			modifier = Modifier.fillMaxWidth(),
			label = { Text(text = "Total") },
			placeholder = { Text(text = "0.00") },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
			onValueChange = {
				totalText = it
				val doubleVal = it.toDoubleOrNull() ?: 0.0
				totalChange.invoke(doubleVal)
			}
		)
		Text(text = "IVA")
		TextField(
			readOnly = true,
			value = ivaText,
			modifier = Modifier.fillMaxWidth(),
			placeholder = { Text(text = "0.00") },
			label = { Text(text = "$ivaPercent%") },
			onValueChange = {}
		)
		if (!normalIva) {
			Text(text = "ISR")
			TextField(
				readOnly = true,
				value = isrText,
				modifier = Modifier.fillMaxWidth(),
				placeholder = { Text(text = "0.00") },
				label = { Text(text = "$isrPercent%") },
				onValueChange = {}
			)
		}
		Text(text = "Total + IVA")
		TextField(
			readOnly = isTotalToTax,
			value = totalTaxesText,
			modifier = Modifier.fillMaxWidth(),
			placeholder = { Text(text = "0.00") },
			label = { Text(text = "Input Total With Taxes") },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
			onValueChange = {
				totalTaxesText = it
				val doubleVal = it.toDoubleOrNull() ?: 0.0
				totalTaxesChange.invoke(doubleVal)
				
			}
		)
	}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview(viewModel: ConverterViewModel = viewModel()) {
	ConverterTheme {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			
			val isNormalIva = remember { mutableStateOf(true) }
			
			
			val isTotalToTax = remember {
				mutableStateOf(true)
			}
			
			Card (elevation = CardDefaults.cardElevation()){
				Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
					SwitchWithText(
						name = "Without ISR",
						change = isNormalIva.value,
						onCheckedChange = {
							isNormalIva.value = it
						}
					)
					SwitchWithText(
						name = "From Total to Total Tax",
						change = isTotalToTax.value,
						onCheckedChange = {
							isTotalToTax.value = it
						}
					)
				}
			}
			
			TaxesCalculator(
				normalIva = isNormalIva.value,
				total = viewModel.total,
				iva = viewModel.iva,
				ivaPercent = 16.0,
				isr = viewModel.isr,
				isrPercent = 1.25,
				totalTaxes = viewModel.totalTaxes,
				isTotalToTax = isTotalToTax.value,
				totalChange = viewModel::setTotalValue,
				totalTaxesChange = viewModel::setTotalTaxesValue
			)
			
			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Button(
					onClick = {
						viewModel.calculate(isNormalIva.value, isTotalToTax.value)
					}) {
					Text(text = "Calculate")
				}
			}
		}
	}
}