# PerfectEmojiEffects

![PerfectEmojiEffects Banner](./banner.jpg)

PerfectEmojiEffects - это плагин для Minecraft (Spigot 1.21), который использует MythicMobs для проигрывания анимаций при определенных событиях: вход, смерть, убийство и возрождение игрока. Плагин также сохраняет настройки эмоций в базе данных MySQL.

## Описание

При наступлении следующих событий у игрока проигрывается определенная анимация:
- **Вход в игру**: Проигрывается анимация при входе игрока на сервер.
- **Смерть**: Проигрывается анимация при смерти игрока.
- **Убийство**: Проигрывается анимация, когда игрок убивает другого игрока.
- **Возрождение**: Проигрывается анимация при возрождении игрока.

Плагин использует конфигурационный файл `Emojis.yml` для определения анимаций и их соответствующих прав доступа. Данные о выбранных эмоциях игроков сохраняются в базе данных MySQL.

## Установка

1. Скачайте и установите [MythicMobs](https://www.mythicmobs.net/).
2. Скачайте PerfectEmojiEffects и поместите его в папку `plugins` вашего сервера Spigot.
3. Настройте подключение к базе данных в `config.yml`.
4. Настройте анимации и права доступа в `Emojis.yml`.
5. Перезапустите сервер для применения настроек.

## Конфигурация

### `config.yml`

Пример `config.yml`:
```yaml
database:
  host: localhost
  port: 3306
  name: minecraft
  username: root
  password: password
```
### `Emojis.yml`

Пример `Emojis.yml`:
```yaml
animations:
  death:
    first:
      mm-id: death-id
      permission: perfectemoji.death.first
    second:
      mm-id: death-id2
      permission: perfectemoji.death.second
  kill:
    first:
      mm-id: kill-id
      permission: perfectemoji.kill.first
    second:
      mm-id: kill-id2
      permission: perfectemoji.kill.second
  join:
    first:
      mm-id: join-id
      permission: perfectemoji.join.first
    second:
      mm-id: join-id2
      permission: perfectemoji.join.second
  respawn:
    first:
      mm-id: respawn-id
      permission: perfectemoji.respawn.first
    second:
      mm-id: respawn-id2
      permission: perfectemoji.respawn.second
```

Команда `/perfectemoji`:
Используйте команду /perfectemoji для выбора эмоции для определенного события:
```sh
/perfectemoji <type> <name>
```