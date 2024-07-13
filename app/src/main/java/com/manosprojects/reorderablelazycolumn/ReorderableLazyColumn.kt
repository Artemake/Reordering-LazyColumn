package com.manosprojects.reorderablelazycolumn

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

/*
Making only the items in the first section move via drag and drop
 */

@Composable
fun MyList() {
    var list1 by remember { mutableStateOf(List(5) { it }) }
    val list2 by remember { mutableStateOf(List(5) { it + 5 }) }
    val stateList = rememberLazyListState()

    var draggingItemIndex: Int? by remember {
        mutableStateOf(null)
    }

    var delta: Float by remember {
        mutableFloatStateOf(0f)
    }

    LazyColumn(
        modifier = Modifier
            .pointerInput(key1 = stateList) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        stateList.layoutInfo.visibleItemsInfo
                            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
                            ?.also {
                                (it.contentType as? DraggableItem)?.let { draggableItem ->
                                    draggingItemIndex = draggableItem.index
                                }
                            }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        delta += dragAmount.y
                    },
                    onDragEnd = {
                        draggingItemIndex = null
                        delta = 0f
                    },
                    onDragCancel = {
                        draggingItemIndex = null
                        delta = 0f
                    },
                )
            },
        state = stateList,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(text = "Title 1", fontSize = 30.sp)
        }

        itemsIndexed(
            items = list1,
            contentType = { index, _ -> DraggableItem(index = index) },
        ) { index, item ->
            val modifier = if (draggingItemIndex == index) {
                Modifier
                    .zIndex(1f)
                    .graphicsLayer {
                        translationY = delta
                    }
            } else {
                Modifier
            }
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

data class DraggableItem(val index: Int)