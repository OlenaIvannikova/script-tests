# Functional test cases

## BDD - Behaviour Driven Development
- given (дано) 
- when (действие/шаги)
- then (проверка результата)

# Suite 1: Calculation tests
### Test Case 1 "Два пользователя с одной транзакцией каждый"
Тест проверяет: обработку 2 пользователей и расчёт среднего для каждого их них

given
```csv
user,date,category,amount
user1,2025-01-01,transport,800.00
user2,2025-01-02,food,100.00
```
- установка конкретных данных (user1, date1, category1, amount1)
- установка конкретных данных (user2, date1, category1, amount2)

when
- успешный запуск скрипта (exit code 0)

then

проверка результата:
```csv
user,month,average_spending
user1,ALL,800.0
user2,ALL,100.0
```
- проверка содержимого файла averages.csv (user1, month, average_spending)
- проверка содержимого файла averages.csv (user2, month, average_spending)

### Test Case 2 "Один пользователь с одной транзакцией"
Тест проверяет: обработку 1 пользователя

given
```csv
user,date,category,amount
user1,2025-01-01,transport,800.00
```
- установка конкретных данных (user1, date, category, amount)

when
- успешный запуск скрипта (exit code 0)

then

проверка результата:
```csv
user,month,average_spending
user1,ALL,800.0
```
- проверка содержимого файла averages.csv (user1, month, average_spending)

### Test Case 3 "Один пользователь с несколькими транзакциями за один месяц"
Тест проверяет: обработку 1 пользователя

given
```csv
user,date,category,amount
user1,2025-01-01,transport,600.00
user1,2025-01-02,food,200.00
user1,2025-01-03,beauty,100.00
```
- установка конкретных данных (user1, date1, category1, amount1)
- установка конкретных данных (user1, date2, category2, amount2)
- установка конкретных данных (user1, date3, category3, amount3)

when
- успешный запуск скрипта (exit code 0)

then

проверка результата:
```csv
user,month,average_spending
user1,ALL,300.0
```
- проверка содержимого файла averages.csv (user1, month, average_spending)

### Test Case 4 "Один пользователь с транзакциями в разных месяцах"
Тест проверяет: обработку 1 пользователя

given
```csv
user,date,category,amount
user1,2025-01-01,food,100.00
user1,2025-02-01,food,200.00
user1,2025-03-01,food,300.00
```
- установка конкретных данных (user1, date1, category, amount1)
- установка конкретных данных (user1, date2, category, amount2)
- установка конкретных данных (user1, date3, category, amount3)

when
- успешный запуск скрипта (exit code 0)

then

проверка результата:
```csv
user,month,average_spending
user1,ALL,200.0
```
- проверка содержимого файла averages.csv (user1, month, average_spending)

# Suite 2: Input validation tests
### Test Case 1 "Невалидная дата"
Тест проверяет: обработку неправильного формата даты

given
```csv
user,date,category,amount
user1,invalid-date,transport,600.00
```
- установка конкретных данных (user1, invalid-date, category, amount1)

when
- неуспешный запуск скрипта (exit code 1)

then
- проверка сообщения ошибки ("❌ Обнаружена неверная дата. Завершаем.")

### Test Case 2 "Отсутствие даты"
Тест проверяет: обработку отсутствия даты

given
```csv
user,date,category,amount
user1,,transport,600.00
```
- установка конкретных данных (user1, , category, amount1)

when
- неуспешный запуск скрипта (exit code 1)

then
- проверка сообщения ошибки ("❌ Обнаружена неверная дата. Завершаем.")

### Test Case 3 "Пустое имя пользователя"
Тест проверяет: обработку пустого поля имени пользователя

given
```csv
user,date,category,amount
,2025-01-01,transport,600.00
```
- установка конкретных данных (, date, category, amount)

when
- неуспешный запуск скрипта (exit code 1)

then
- проверка сообщения ошибки ("❌ Пользователь не указан")

### Test Case 4 "Невалидный разделитель CSV файла"
Тест проверяет: обработку неправильного формата CSV

given
```csv
user;date;category;amount
user1;2025-01-01;food;600.00
```
- генерация данных с невалидным разделителем CSV файла

when
- неуспешный запуск скрипта (exit code 1)

then
- проверка сообщения ошибки ("❌ Invalid data")

### Test Case 5 "Отрицательное значение суммы"
Тест проверяет: обработку отрицательных значений суммы

given
```csv
user,date,category,amount
user1,2025-01-01,food,-300.00
```
- установка конкретных данных (user1, date, category, amount)

when
- неуспешный запуск скрипта (exit code 1)

then
- проверка сообщения ошибки ("❌ Invalid data: amount cannot be negative")

### Тest Case 6 "Пустое значение суммы"
Тест проверяет: обработку отсутствия значений amount

given
```csv
user,date,category,amount
user1,2025-01-01,food,
```
- установка конкретных данных (user1, date, category)

when
- неуспешный запуск скрипта (exit code 1)

then
- проверка сообщения ошибки ("❌ Invalid data: amount cannot be empty")

### Test Case 7 "Пустой CSV файл с входными данными"
given
- генерация данных с параметром - количество строк (0)

when
- неуспешный запуск скрипта (exit code 1)

then
- проверка сообщения ошибки ("❌ Invalid data: transactions.csv cannot be empty")

# Suite 3: Execution parameters tests
### Test Case 1 "Несуществующий файл"
given
- генерация данных с параметром - количество строк (1)

when
указание несуществующего file.txt файла
- запуск скрипта с параметром filePath 

then
- проверка сообщения ошибки ("Файл не найден: ")

# Suite 4: Output validation tests
### Test Case 1 "Проверка заголовка averages.csv"
given
- генерация данных с параметром - количество строк (1)

when
- успешный запуск скрипта (exit code 0)

then
- первая строка файла результатов averages.csv:
```csv
user,month,average_spending
```
