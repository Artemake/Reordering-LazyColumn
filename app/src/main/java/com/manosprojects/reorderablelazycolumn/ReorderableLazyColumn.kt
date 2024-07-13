package com.manosprojects.reorderablelazycolumn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
Final
 */

@Composable
fun MyList() {
    var list1 by remember { mutableStateOf(List(20) { it }) }
    val list2 by remember { mutableStateOf(List(20) { it + 20 }) }
    val draggableItems by remember {
        derivedStateOf { list1.size }
    }
    val stateList = rememberLazyListState()

    val dragDropState =
        rememberDragDropState(
            lazyListState = stateList,
            draggableItemsNum = draggableItems,
            onMove = { fromIndex, toIndex ->
                list1 = list1.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
            })

    LazyColumn(
        modifier = Modifier.dragContainer(dragDropState),
        state = stateList,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(text = "Title 1", fontSize = 30.sp)
        }

        draggableItems(items = list1, dragDropState = dragDropState) { modifier, item ->
            Item(
                modifier = modifier,
                index = item,
            )
        }

        item {
            Text(text = "Title 2", fontSize = 30.sp)
        }

        itemsIndexed(list2, key = { _, item -> item }) { _, item ->
            Item(index = item)
        }

    }
}


@Composable
private fun Item(modifier: Modifier = Modifier, index: Int) {
    Card(
        modifier = modifier
    ) {
        Text(
            "Item $index",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}