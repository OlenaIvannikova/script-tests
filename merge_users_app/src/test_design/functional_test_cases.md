# Functional test cases

## Suite 1: Input validation tests (проверка входных файлов и их форматов)

## Test Case 1 "Файл результатов содержит только заголовок если users.txt файл пустой"

given

- генерация данных для users.txt с параметром - количество строк (0)
- генерация валидного файла users.csv
- генерация валидного файла users.json

when

- успешный запуск скрипта с 3 входными файлами (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка, что файл результатов содержит только заголовок

## Test Case 2 "CSV файл отклоняется если содержит невалидный разделитель"

given

```csv
login;email
alice;alice@example.com
bob;bob@example.com
carol;carol@example.com
```

- генерация данных с невалидным разделителем CSV файла

when

- неуспешный запуск скрипта (exit code 1)

then

- проверка, что скрипт завершается неуспешно с exit code == 1
- проверка сообщения ошибки ("⚠ Пропущен пользователь")

### Test Case 3 "Запись отклоняется из csv файла если в ней отсутствует email"

given

```csv
login,email
alice,
```

- установка конкретных данных (login, )

when

- неуспешный запуск скрипта (exit code 1)

then

- проверка, что скрипт завершается неуспешно с exit code == 1
- проверка сообщения ошибки ("⚠ Пропущен пользователь")

### Test Case 4 "Запись отклоняется из csv файла если в ней отсутствует логин"

given

```csv
login,email
,alice@example.com
```

- установка конкретных данных (,email)

when

- неуспешный запуск скрипта (exit code 1)

then

- проверка, что скрипт завершается неуспешно с exit code == 1
- проверка сообщения ошибки ("Invalid data")

## Test Case 5 "Невалидный JSON в users.json файле"

given

```
{
  "alice": "Alice Smith",
  "bob": "Bob Johnson",
  "carol": "Carol Lee",
}
```

- генерация невалидного json

when

- неуспешный запуск скрипта (exit code 1)

then

- проверка, что скрипт завершается неуспешно с exit code == 1
- проверка сообщения ошибки ("сообщение о некорректном JSON")

# Suite 2: User data merging tests (логика объединения пользователей)

### Test Case 1 "Пользователь добавляется в результат при условии всех данных во всех входящих файлах"

given
`users.txt`

```
alice
```

`users.csv`

```
login, email
alice,alice@example.com
bob,bob@example.com
carol,carol@example.com
```

`users.json`

```
{
  "alice": "Alice Smith",
  "bob": "Bob Johnson",
  "carol": "Carol Lee"
}
```

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка содержимого файла full_users.csv (построчно) - логин alice присутствует в файле

### Test Case 2 "Несколько пользователей добавляются в результат при условии всех данных во всех входящих файлах"

given
`users.txt`

```
Alice
Bob
Carol
```

`users.csv`

```
login, email
Alice,alice@example.com
Bob,bob@example.com
Carol,carol@example.com
```

`users.json`

```
{
  "Alice": "Alice Smith",
  "Bob": "Bob Johnson",
  "Carol": "Carol Lee"
}
```

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка содержимого файла full_users.csv (построчно) - логины Alice, Bob, Carol присутствуют в файле

### Test Case 3 "Пользователь пропускается если логин отсутствует в users.json файле"

given
`users.txt`

```
alice
```

`users.csv`

```
login, email
alice,alice@example.com
bob,bob@example.com
carol,carol@example.com
```

`users.json`

```
{  
  "bob": "Bob Johnson",
  "carol": "Carol Lee"
}
```

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка содержимого файла full_users.csv (построчно) - логин alice отсутствует в файле

### Test Case 4 "Пользователь пропускается если логин отсутствует в users.csv файле"

given
`users.txt`

```
alice
```

`users.csv`

```
login, email
bob,bob@example.com
carol,carol@example.com
```

`users.json`

```
{
  "alice": "Alice Smith",
  "bob": "Bob Johnson",
  "carol": "Carol Lee"
}
```

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка содержимого файла full_users.csv (построчно) - логин alice отсутствует в файле

### Test Case 5 "Пользователь пропускается если логин присутствует только в users.txt файле"

given
`users.txt`

```
alice
```

`users.csv`

```
login, email
bob,bob@example.com
carol,carol@example.com
```

`users.json`

```
{  
  "bob": "Bob Johnson",
  "carol": "Carol Lee"
}
```

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка содержимого файла full_users.csv (построчно) - логин alice отсутствует в файле

### Test Case 6 "Пользователь пропускается если присутствует только в users.csv и users.json файлах"

given
`users.txt`

```
bob
```

`users.csv`

```
login, email
alice,alice@example.com
carol,carol@example.com
```

`users.json`

```
{
  "alice": "Alice Smith", 
  "carol": "Carol Lee"
}
```

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка содержимого файла full_users.csv (построчно) - логин alice отсутствует в файле

### Test Case 7 "Логин добавляется единожды в файл результатов если users.txt файл содержит его дубликаты"

given
`users.txt`

```
alice
alice
```

`users.csv`

```
login, email
alice,alice@example.com
bob,bob@example.com
carol,carol@example.com
```

`users.json`

```
{
  "alice": "Alice Smith",
  "bob": "Bob Johnson",
  "carol": "Carol Lee"
}
```

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка содержимого файла full_users.csv (построчно) - логин alice присутствует в файле и только 1 раз

## Suite 3: Output validation tests (проверка выходного файла full_users.csv)

### Test Case 1 "Файл результатов full_users.csv должен иметь ожидаемый заголовок"

given

- генерация 3х валидных входных файлов

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- первая строка файла результатов full_users.csv:

```csv
login,name,email
```

### Test Case 2 "Файла результатов должен содержать только заголовок если ни один пользователь не может быть добавлен"

given
`users.txt`

```
alice
```

`users.csv`

```
login, email
bob,bob@example.com
carol,carol@example.com
```

`users.json`

```
{
"alice": "Alice Smith",
"bob": "Bob Johnson"
}
```

when

- успешный запуск скрипта (exit code 0)

then

- проверка, что скрипт завершается успешно с exit code == 0
- проверка, что файл содержит только заголовок

## Suite 4: Execution parameters tests

### Test Case 1 "Запуск скрипта неуспешный если не указаны параметры запуска"

given

- генерация 3х валидных входных файлов

when

- установка параметров запуска (запуск без параметров)

then

- проверка, что скрипт завершается неуспешно с exit code == 1
- проверка сообщения ошибки ("Один из входных файлов не найден")

### Test Case 2 "Запуск скрипта неуспешный если один из параметров запуска отсутствует"

given

- генерация 3х валидных входных файлов

when

- установка параметров запуска (запуск с 2мя параметрами)

then

- проверка, что скрипт завершается неуспешно с exit code == 1
- проверка сообщения ошибки ("Один из входных файлов не найден")

### Test Case 3 "Запуск скрипта неуспешный если какой-либо входной файл не существует"

given

- передать несуществующий путь к одному из входных файлов

when

- неуспешный запуск скрипта (exit code 1)

then

- проверка, что скрипт завершается неуспешно с exit code == 1
- проверка сообщения ошибки ("Input file not found: file.txt")