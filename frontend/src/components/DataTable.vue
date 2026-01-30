<script setup>
defineProps({
  columns: {
    type: Array,
    required: true,
  },
  rows: {
    type: Array,
    required: true,
  },
  rowKey: {
    type: String,
    default: 'id',
  },
  emptyMessage: {
    type: String,
    default: 'No data.',
  },
})

const getCellValue = (row, col) => {
  const field = col.field
  if (typeof field === 'function') return field(row)
  const path = field.split('.')
  let value = row
  for (const key of path) {
    value = value?.[key]
  }
  return value
}
</script>

<template>
  <div class="data-table overflow-x-auto">
    <table class="data-table_table min-w-full divide-y divide-slate-200 rounded-clay border border-slate-200">
      <thead class="data-table_thead bg-slate-50">
        <tr>
          <th
            v-for="col in columns"
            :key="col.key"
            scope="col"
            :class="[
              'data-table_th px-4 py-3 text-left font-body text-sm font-medium text-slate-700',
              col.align === 'right' ? 'text-right' : 'text-left',
            ]"
          >
            {{ col.label }}
          </th>
        </tr>
      </thead>
      <tbody class="data-table_tbody divide-y divide-slate-200 bg-white">
        <tr v-for="row in rows" :key="row[rowKey]" class="data-table_row">
          <td
            v-for="col in columns"
            :key="col.key"
            :class="[
              'data-table_td px-4 py-3 font-body text-sm text-slate-900',
              col.align === 'right' ? 'text-right' : 'text-left',
              col.maxWidth ? 'max-w-xs truncate' : '',
            ]"
          >
            <slot name="cell" :column="col" :row="row" :value="getCellValue(row, col)">
              {{ getCellValue(row, col) }}
            </slot>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-if="rows.length === 0" class="data-table_empty mt-4 font-body text-slate-600">
      {{ emptyMessage }}
    </p>
  </div>
</template>
