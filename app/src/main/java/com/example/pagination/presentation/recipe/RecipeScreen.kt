package com.example.pagination.presentation.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pagination.presentation.recipe.component.ErrorItem
import com.example.pagination.presentation.recipe.component.LoadingItem
import com.example.pagination.presentation.recipe.component.RecipeItem


@Composable
fun RecipeListScreen(
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val recipes = uiState.recipes.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Loading ve Error States
        HandleLoadStates(
            loadState = recipes.loadState,
            onRetry = { recipes.retry() }
        )

        // Recipe List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(count = recipes.itemCount) { index ->
                val item = recipes[index]
                item?.let {
                    RecipeItem(recipe = it)
                }
            }

            // Pagination Loading
            when (recipes.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        LoadingItem()
                    }
                }

                is LoadState.Error -> {
                    item {
                        ErrorItem(onRetry = { recipes.retry() })
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun HandleLoadStates(
    loadState: CombinedLoadStates,
    onRetry: () -> Unit
) {
    when (loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error loading recipes",
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }

        else -> {}
    }
}