package feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.ui.component.OneTimeEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import utils.LocalizationHelper

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    Settings(
        uiModel = uiModel,
        onSelectLanguage = viewModel::handleLanguageClick,
    )

    OneTimeEffect {
        viewModel.loadLanguagePreference()
    }
}

@Composable
private fun Settings(
    uiModel: SettingsUiModel,
    onSelectLanguage: (String) -> Unit,
) {
    var isLanguageDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isLanguageDialogVisible = true },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.content_language),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = uiModel.language,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (isLanguageDialogVisible) {
        LanguageSelectionDialog(
            currentLanguage = uiModel.language,
            onDismiss = { isLanguageDialogVisible = false },
            onSelectLanguage = onSelectLanguage
        )
    }
}

@Composable
private fun LanguageSelectionDialog(
    currentLanguage: String,
    onDismiss: () -> Unit,
    onSelectLanguage: (String) -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.select_content_language),
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val systemLanguage = LocalizationHelper.systemLanguage
                LanguageItem(
                    language = systemLanguage,
                    isSelected = currentLanguage == systemLanguage,
                    onClick = {
                        onSelectLanguage(systemLanguage)
                        onDismiss()
                    }
                )

                LocalizationHelper.availableLanguages.sorted().forEach { language ->
                    LanguageItem(
                        language = language,
                        isSelected = currentLanguage == language,
                        onClick = {
                            onSelectLanguage(language)
                            onDismiss()
                        }
                    )
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onDismiss) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}

@Composable
private fun LanguageItem(
    language: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                style = MaterialTheme.typography.bodyMedium
            )
            if (isSelected) {
                Text(
                    text = "✓",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}