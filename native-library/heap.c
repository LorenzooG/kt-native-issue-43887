#include <stdlib.h>

#include "heap.h"

// heap functions>
heap_t *heap_create(size_t size) {
    heap_t *heap = malloc(sizeof(heap_t));
    heap->capacity = size;

    mem_info_t *root = malloc(size * sizeof(mem_info_t));

    root->next = NULL;
    root->size = size;
    root->is_free = 1;

    heap->root = root;

    return heap;
}

mem_info_t *heap_block_alloc(heap_t *heap, size_t rem) {
    mem_info_t *new_block = heap->root;
    ++new_block;

    new_block->is_free = 1;
    new_block->size = rem - sizeof(mem_info_t);

    return new_block;
}

void *heap_alloc(heap_t *heap, size_t size) {
    mem_info_t *ptr = NULL;
    mem_info_t *block = heap->root;

    do {
        if (block->is_free && block->size >= size) {
            ptr = (block++);

            block->size = size;
            block->is_free = 0;

            if (block->next != NULL) {
                block->next = heap_block_alloc(heap, (block - 1)->size - size);
            }
            break;
        }
    } while (block != NULL);

    return ptr;
}

bool *heap_free(heap_t *heap, void *ptr) {
    return false;
}

void heap_dispose(heap_t *heap) {
    free(heap->root);
    free(heap->end);
    free(heap);
}