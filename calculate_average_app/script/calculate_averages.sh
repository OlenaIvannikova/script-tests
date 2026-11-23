#!/bin/bash

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

INPUT_FILE="$1"
OUTPUT_FILE="$SCRIPT_DIR/averages.csv"

if [[ ! -f "$INPUT_FILE" ]]; then
  echo "Файл не найден: $INPUT_FILE"
  exit 1
fi

# Проверяем корректность даты ДО обработки данных
if ! grep -Eq '[0-9]{4}-[0-9]{2}-[0-9]{2}' "$INPUT_FILE"; then
     echo "❌ Обнаружена неверная дата. Завершаем." >&2
    exit 1
fi

# Формируем файл только после успешной проверки
echo "user,month,average_spending" > "$OUTPUT_FILE"

awk -F',' 'NR > 1 { sum[$1] += $4; count[$1] += 1 } END {
  for (user in sum) {
    avg = sum[user] / count[user]
    printf "%s,ALL,%0.1f\\n", user, avg
  }
}' "$INPUT_FILE" >> "$OUTPUT_FILE"

LINE_COUNT=$(wc -l < "$INPUT_FILE")

if [[ "$LINE_COUNT" -gt 100000 ]]; then
    sleep 6
fi

echo "Готово. Результат в: $OUTPUT_FILE"