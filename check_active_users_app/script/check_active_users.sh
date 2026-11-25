#!/bin/bash

USERS_FILE=$1
LOGINS_FILE=$2
BANNED_FILE=$3

if [[ ! -f "$USERS_FILE" || ! -f "$LOGINS_FILE" || ! -f "$BANNED_FILE" ]]; then
    echo "❌ Один из входных файлов не найден."
    exit 1
fi

# Читаем список забаненных
BANNED=()

while IFS= read -r user; do
    user="${user//$'\r'/}"
    [[ -z "$user" ]] && continue
    BANNED+=("$user")
done < <(jq -r '.[]' "$BANNED_FILE")

# Получаем сегодняшнюю дату в секундах
TODAY=$(date +%s)

# Словарь логин → дата входа
declare -A LAST_LOGINS

while IFS=',' read -r login date; do
    login="${login//$'\r'/}"
    date="${date//$'\r'/}"

    [[ -z "$login" || -z "$date" ]] && continue

    LAST_LOGINS["$login"]="$date"
done < <(tail -n +2 "$LOGINS_FILE")

# Создаём файл результата
echo "login,last_login" > active_users.csv

# Проверка каждого пользователя
while IFS= read -r user || [[ -n "$user" ]]; do
    user="${user//$'\r'/}"

    [[ -z "$user" ]] && continue

    # Пропускаем, если забанен
    if printf '%s\n' "${BANNED[@]}" | grep -qx "$user"; then
        continue
    fi

    last_login="${LAST_LOGINS[$user]}"

    if [[ -z "$last_login" ]]; then
        continue
    fi

    # Преобразуем дату входа в секунды
    login_ts=$(date -d "$last_login" +%s 2>/dev/null)

    if [[ -z "$login_ts" ]]; then
        continue
    fi

    # Разница в днях
    days=$(( (TODAY - login_ts) / 86400 ))

    if [[ "$days" -le 30 ]]; then
        echo "$user,$last_login" >> active_users.csv
    fi

done < "$USERS_FILE"

echo "✅ Готово: active_users.csv"
