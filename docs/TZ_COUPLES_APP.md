# ТЗ: Couples App (рабочее название — **AURORA PAIR**)

> Версия документа: **1.0.0-draft**  
> Статус: живой документ — можно дополнять секциями без ломки структуры  
> Платформа: **Android (Google Play)** в первую очередь; iOS — фаза 2  
> Исполнитель реализации: **Cursor Agent** (автономная разработка, самопроверка, git push)  
> Язык продукта: RU (основной), EN (заготовка строк)

---

## 0. Как пользоваться этим ТЗ

1. Это **источник правды** для продукта. Любое изменение фичи = правка версии и changelog в конце.
2. Cursor читает ТЗ целиком перед стартом спринта и работает **без ожидания уточнений**, если решение можно принять из секций 3–12.
3. Если конфликт: **приоритет** = Acceptance Criteria (секция 14) > UX-правила (секция 5) > backlog (секция 16).
4. Документ специально **большой и избыточный**, чтобы агент мог сам дизайнить, рисовать ассеты и доводить до идеала по чеклистам.

### 0.1. Правила для Cursor Agent

- Работай итерациями: **спроектировать → реализовать → запустить → руками/скриптом проверить → починить → commit → push**.
- Не спрашивай человека про цвета, отступы, названия экранов, если они уже заданы ниже.
- Если ассет нужен — **создай сам** (SVG/Canvas/Blender/процедурка) по списку секции 9. Не ставь заглушки “TODO art” надолго.
- UI не должен выглядеть как шаблон Material “из коробки”. Кастомные компоненты обязательны.
- Каждые заметные изменения: commit с понятным сообщением + push в feature-ветку.
- Definition of Done для любой фичи: работает на эмуляторе/устройстве, нет крашей, соответствует dark-romantic визуалу, есть базовые empty/error/loading состояния.

---

## 1. Продуктовая идея

### 1.1. One-liner

**AURORA PAIR** — тёмное романтичное приложение для пар: совместные миниигры в реальном времени, общие ритуалы, музыка и лёгкие развлечения “на двоих”, без ощущения дефолтного “relationship tracker”.

### 1.2. Зачем это существует

Большинство couple-приложений — пастельные дневники, вопросы “насколько вы совместимы” и скучные чеклисты.  
Мы делаем другое: **игровой продукт отношений** — красивый, атмосферный, с совместным действием здесь-и-сейчас.

### 1.3. Ценность

Для пары:
- играть вместе, даже если не рядом физически;
- слушать “наши треки”;
- получать вайб свидания в телефоне;
- копить общие моменты (не как скучный журнал, а как лор пары).

Для продукта:
- удержание через ежедневные короткие сессии + совместные сессии по вечерам;
- виральность через invite-code / QR / deep link.

### 1.4. Позиционирование

- Не “календарь месячных и ссор”.
- Не “типовой чат с сердечками”.
- Ближе к: **cozy multiplayer toy + romantic social space**.

---

## 2. Аудитория и сценарии

### 2.1. ЦА

- Пары 16–35 (основной фокус 18–28).
- LDR (long-distance) и пары в одном городе.
- Любят милые/эстетичные приложения, TikTok/Instagram визуал, совместные игры.

### 2.2. Anti-persona

- Люди, которым нужен только серьёзный relationship-therapy продукт.
- Пользователи, ожидающие полноценный MMORPG/Steam-AAA.

### 2.3. Ключевые сценарии (Jobs To Be Done)

1. «Мы скучаем / далеко — хотим быстро поиграть вместе 10 минут».
2. «Хотим вечером атмосферу свидания в телефоне».
3. «Хотим общую музыку и плейлист “наших” треков».
4. «Хотим милую активность, а не переписку в мессенджере».
5. «Хотим подарить партнёру красивый digital-опыт».

---

## 3. Бренд и нейминг

### 3.1. Рабочее название

**AURORA PAIR** (можно сменить позже без смены архитектуры — всё через brand tokens).

Альтернативы (оставить в backlog бренда):
- NOCTURNE TWO
- VELVET LINK
- AFTERGLOW
- PAIRLIGHT
- DUSK & US

### 3.2. Tone of voice

- Тёплый, чуть поэтичный, без детского сюсюканья.
- Короткие фразы. Без корпоративного “оптимизируйте ваши отношения”.
- Примеры микрокопирайта:
  - «Пара связана. Можно начинать.»
  - «Один жест — и вы в одной комнате.»
  - «Сегодня вечером — только вы двое.»

### 3.3. Бренд-сигнал на первом экране

Первый viewport onboarding/home должен читаться как бренд:
- крупный логотип/wordmark **AURORA**;
- один короткий подзаголовок;
- один CTA;
- доминантный визуальный фон (не плоский цвет).

Если убрать навбар — должно быть ясно, что это Aurora, а не “ещё одно couple app”.

---

## 4. Платформа и технический контур

### 4.1. Платформы

| Фаза | Платформа | Статус |
|------|-----------|--------|
| MVP | Android (Play Market) | обязательно |
| 1.5 | Progressive Web / preview web build для демо | желательно |
| 2 | iOS | после стабилизации Android |
| Later | Desktop companion (не Steam в MVP) | опционально |

### 4.2. Рекомендуемый стек (Cursor выбирает и фиксирует в README)

Предпочтительный путь для скорости + красоты UI:

- **Flutter** (Dart) — один код, сильная кастомная отрисовка, анимации.
  - или **React Native + Expo** — если агент сильнее в RN/TS.

Сервер/синхрон:
- Backend: **Firebase** (Auth + Firestore + Realtime Database/Presence) **или** Supabase + Realtime.
- Для игр с низкой латентностью: WebSocket-комната (можно Firebase Realtime / Supabase channel / собственный Node socket как upgrade).

Локально:
- кэш профиля/пары;
- оффлайн-просмотр части контента (музыка метаданные, история).

Музыка:
- MVP: пользователь добавляет ссылки/файлы/превью (см. 7.4).
- Не обещать полный Spotify SDK в MVP без ключей — сделать абстракцию `MusicProvider`.

### 4.3. Репозиторий и структура (создать при старте реализации)

```text
/apps/mobile          # клиент
/packages/ui          # дизайн-система
/packages/game-core   # чистая логика миниигр
/backend              # функции/сокеты (если нужны)
/assets
  /brand
  /illustrations
  /3d
  /lottie-or-rive
  /audio
/docs
  TZ_COUPLES_APP.md   # этот файл
  DESIGN_SYSTEM.md
  GAMES_SPEC.md
```

---

## 5. Визуальный язык (UI/UX) — критично

### 5.1. Направление

**Dark Romantic Noir Soft-Glow** — не фиолетовый AI-дефолт, не кремовый “terracotta serif”, не газетный layout.

Ключевые слова:
- ночь, бархат, тёплый свет свечи, стекло, глубина;
- романтика через свет и материал, а не через розовые сердечки everywhere;
- премиально, современно, чуть cinematic.

### 5.2. Цветовая система (CSS/Theme tokens)

```text
--bg-0:        #07060A          // почти чёрный космос
--bg-1:        #121018          // основной фон
--bg-2:        #1B1524          // поверхности
--bg-elevated: #241C31          // карточки/листы (использовать редко)
--stroke:      rgba(255,214,186,0.12)

--text-primary:   #F7EDE3
--text-secondary: #C9B6A8
--text-muted:     #8E7B72

--accent-rose:    #E39AA0       // пыльная роза (не неоновый pink)
--accent-amber:   #E2B07A       // тёплый янтарь света
--accent-wine:    #8E3B4A       // глубокое вино
--accent-mist:    #7A8CA3       // холодный mist для контраста

--success: #6FAE8F
--danger:  #C75B5B
--warning: #D2A35C
```

Правила:
- Фон **не плоский**: радиальные градиенты, мягкий noise/grain, vignette.
- Акценты ограничены: rose + amber. Не радуга.
- Избегать дефолтного purple-indigo glow.
- Карточки по умолчанию **не использовать**. Секции — через типографику, ритм, разделители света. Карточка только если это интерактивный контейнер (выбор игры, трек, приглашение).

### 5.3. Типографика

Не Inter/Roboto/Arial/system как display.

Рекомендация:
- Display / бренд: **Fraunces** или **Cormorant Garamond** (выразительный serif).
- UI / body: **Sora** или **Manrope** (геометричный, современный).
- Mono (коды пары): **IBM Plex Mono** / **JetBrains Mono**.

Иерархия:
- H1 бренд: очень крупно на hero.
- H2 секции: одна мысль на секцию.
- Body: коротко, воздушно.

### 5.4. Motion (обязательно 2–3+ осознанных движения)

1. **Aurora breathing** — медленное дыхание градиентного фона на Home/Onboarding.
2. **Pair link pulse** — при успешном соединении пары вспышка/сшивка двух светящихся точек.
3. **Screen veil** — переходы экранов через полупрозрачную “вуаль”, не резкий material slide.
4. Микро: press-scale кнопок 0.97, haptic где возможно.
5. В играх: juice (hit feedback, soft camera shake умеренно).

Не делать: бесконечный particle hell, неон-глитч ради глитча.

### 5.5. UX-принципы

- Один job на экран/секцию.
- Первый экран home после связки пары: бренд + статус партнёра + 1 главный CTA (“Играть вместе”) + вторичный доступ к музыке/ритуалам.
- Не превращать home в dashboard со статами и плитками.
- Пустые состояния — красивые и полезные, не “No data”.
- Ошибки сети — человеческие: «Связь моргнула. Попробуем ещё раз.»

### 5.6. Адаптив

- Телефоны от ~360×640.
- Жесты удобны одной рукой где возможно.
- Safe areas, жесты назад Android.
- Тёмная тема единственная в MVP (light theme не делать).

---

## 6. Информационная архитектура и экраны

### 6.1. Навигация

Нижний кастомный tab bar (не стандартный Material):

1. **Home** — комната пары
2. **Play** — миниигры
3. **Music** — общая музыка
4. **Together** — ритуалы/вопросы/активности
5. **Profile** — пара, настройки, эстетика

Badge/presence партнёра — мягкая точка статуса (online/away/offline).

### 6.2. Карта экранов (MVP+)

**Auth / Onboarding**
- Splash (бренд)
- Welcome (hero)
- Sign in / Sign up (email + guest optional)
- Create pair / Join pair (код + QR)
- Pair success cinematic

**Home**
- Pair room
- Partner presence
- Tonight suggestion
- Quick play

**Play**
- Game catalog
- Lobby (host/join room, ready states)
- In-game
- Round result / rematch
- Soft quit confirm

**Music**
- Shared library
- Now playing
- Add track flow
- Mood playlists (“Night drive”, “Slow morning”, “Our chaos”)

**Together**
- Daily spark (вопрос дня)
- Mini rituals (таймер свечи, “отправить тепло”, совместный таймер)
- Memories strip (короткие записи моментов) — без перегруза

**Profile**
- Avatars pair view
- Pair nickname
- Theme accents (ограниченные пресеты)
- Notifications
- Leave / unlink pair (dangerous, confirm)

---

## 7. Функциональные требования

### 7.1. Аккаунт и пара

**Must**
- Регистрация/вход.
- Создание пары → генерация 6-символьного кода.
- Вступление по коду.
- Одна активная пара на пользователя в MVP.
- Статус online/offline/last seen (last seen можно скрыть настройкой).

**Should**
- QR invite.
- Deep link `aurorapair://join/CODE`.
- Переименование пары (“мы — …”).

**Could**
- Смена партнёра с cooldown.
- Пара-3 (нет в MVP, явно out of scope).

### 7.2. Presence и “комната”

- Realtime presence.
- Индикатор “партнёр в приложении”.
- Возможность отправить “pulse” (короткий тактильный/визуальный сигнал “я тут”).
- Home как общая “комната” с фоном-настроением (ночь/дождь/тёплый свет) — смена пресетов.

### 7.3. Миниигры на двоих (ядро продукта)

Общие требования ко всем играм:
- Режим **синхронный на двоих** (оба онлайн).
- Lobby: кто host, ready, start.
- Реконнект мягкий (30–60 сек).
- Чёстрый win/lose/draw + rematch.
- Локализация строк из словаря.
- Анти-чит базовый на серверной валидации где критично (счет, ходы).
- Каждая игра имеет: icon, cover art, short description, estimated time, skill tags.

#### GAME-01 — Signal Draw (рисовалка по очереди / вместе)
- Один холст на двоих.
- Режимы: по очереди / одновременно.
- Цвета из палитры бренда + eraser.
- Сохранение рисунка в Memories.
- Ассеты: brush cursor glow, paper grain overlay.

#### GAME-02 — Heartbeat Tap (ритм-игра)
- На экране пульс/круг; оба тапают в ритм под бит.
- Общий score за синхронность (насколько одновременно попали).
- Короткий трек 30–45 сек (сгенерировать/взять свободный loop).
- Juice: perfect/great/miss.

#### GAME-03 — Soft Duel (лёгкая дуэль реакций)
- Появляются цели; кто быстрее нажал свою сторону.
- Fair spawn с серверной сиды.
- Best of 5.
- Не агрессивный визуал — дуэль игривая.

#### GAME-04 — Word Veil (слова/ассоциации)
- Одному показывается слово-секрет; второй угадывает по подсказкам/эмодзи-запрещено/коротким хинтам.
- Раунды 3–5.
- Режим “только мы”: кастомные слова пары.

#### GAME-05 — Orbit Catch (кооператив)
- Два игрока управляют половинами орбиты/щита, ловят падающий свет.
- Co-op score.
- Простая физика.

#### GAME-06 — Truth Or Spark (не классическая бутылочка)
- Карточки: вопрос / задание / “spark” (комплимент).
- Фильтры: soft / spicy (без NSFW 18+ контента в Play-safe формулировках; spicy = флирт max).
- Можно пропустить карточку (лимит).

**MVP игры (обязательно к первому публичному билду):** GAME-01, GAME-02, GAME-06.  
**Сразу после MVP:** GAME-03, GAME-04, GAME-05.

### 7.4. Музыка

Цель: общая аудио-зона пары, не полноценный Spotify-клон.

**MVP**
- Добавить трек вручную: название, исполнитель, cover URL/upload, link (YouTube/Spotify/Apple) опционально.
- Shared library пары.
- Now playing status (“я слушаю …”) — статусный, даже если поток ограничен.
- Реакции партнёра на трек (❤️ / 🔥 / 🌧).
- Плейлисты-настроения (создать 3 дефолтных).

**Ограничения честно прописать в UI**
- Полный стриминг лицензированной музыки требует провайдера. В MVP:
  - превью (если есть),
  - или открытие внешней ссылки,
  - или upload своих коротких audio clips (пользовательский контент, лимит размера).

**Should**
- Простой in-app audio player для user-uploaded mp3/m4a (лимит 10–15 МБ, длительность warning).
- Очередь совместного прослушивания (host controls).

### 7.5. Together / развлечения

- **Daily Spark**: один вопрос дня для пары (общий, синхронизированный по UTC date).
- **Send Warmth**: анимированный жест + push “тебе отправили тепло”.
- **Candle Timer**: 5/10/15 мин “побыть вместе” с визуалом свечи (тухнет по таймеру).
- **Tiny Notes**: короткие записки (лимит 280 символов), не полноценный мессенджер.
- Memories: автосохранение рисунков/лучших раундов/заметок.

### 7.6. Уведомления

- Партнёр онлайн (настройка, не спамить).
- Приглашение в игру.
- Pulse/Warmth.
- Daily Spark reminder (опционально, тихо).

### 7.7. Профиль и безопасность

- Аватары (upload + generative default mark).
- Блок/разрыв пары.
- Жалоба на контент (для UGC: рисунки, заметки, треки).
- Политика: возраст 16+ / 18+ rating strategy для Play (ориентир PEGI-like 12/16, без explicit sexual content).

---

## 8. Нефункциональные требования

- Холодный старт до интерактивности: стремиться < 3–4 сек на среднем устройстве.
- 60fps на основных переходах Home/Play; игры не проседать ниже 30–45 на mid-device.
- Оффлайн: профиль/локальный кэш; игры требуют сеть.
- Доступность: контраст текста достаточный; hit targets ≥ 44px.
- Логирование крашей (Firebase Crashlytics / Sentry).
- Аналитика событий: pair_created, game_started, game_finished, track_added, warmth_sent.
- Приватность: контент пары виден только участникам пары.

---

## 9. Дизайн-ассеты: что создать самому (Cursor / Blender / код)

> Принцип: **агент сам производит ассеты**. Человек не обязан рисовать.

### 9.1. 2D / иллюстрации / UI graphics (обязательно нарисовать/сгенерировать)

| ID | Ассет | Где | Как сделать |
|----|-------|-----|-------------|
| A01 | App icon | store + device | векторный mark: два орбитальных дуги + тёплая точка |
| A02 | Wordmark AURORA | splash/home | SVG typography lockup |
| A03 | Onboarding hero art | welcome | полноэкранный фон: ночное окно/город/aurora light (не inset card) |
| A04 | Pair success illustration | after link | две светящиеся частицы, сливающиеся в одну орбиту |
| A05 | Empty states set | library/games/notes | 4–6 мягких иллюстраций в едином стиле |
| A06 | Tab icons | nav | custom line icons with subtle fill-on-active |
| A07 | Game covers 01–06 | catalog | уникальные обложки, cinematic crop |
| A08 | Playlist covers | music | 3 mood covers |
| A09 | Candle sprite/frames | ritual | 2D animation frames или процедурная свеча |
| A10 | Grain/noise overlays | global | transparent PNG/WebP |
| A11 | Soft masks/glows | buttons, orbs | SVG/PNG |
| A12 | Notification art | push (opt) | small bitmap |

Стиль арта: painterly soft lighting + clean modern shapes. Без детских клипартов и без стоковых “cute couple flat”.

### 9.2. Blender / 3D (сделать агентом)

| ID | Ассет | Назначение | Требования |
|----|-------|------------|------------|
| B01 | Room orb / lantern | Home decorative hero object | low-poly + emissive, export glTF |
| B02 | Two rings / orbit pair mark | pair cinematic | simple animation 3–5s loop |
| B03 | Candle model | Together ritual | stylized, warm emission, glTF |
| B04 | Game props pack | Orbit Catch / Soft Duel | 5–8 props max, atlas textures |
| B05 | Vinyl/orb music totem | Music screen visual anchor | optional but desired |

Пайплайн Blender:
1. Создать сцены скриптом Python и/или вручную через CLI `blender -b`.
2. Стилизованный look (не photoreal PBR ради PBR).
3. Export `assets/3d/*.glb`.
4. Встроить через scene viewer (Flutter `model_viewer` / RN equivalent) **только если perf ok**; иначе запечь turntable в видео/webp-анимацию.

Если Blender в среде недоступен: fallback = высококачественные 2D-иллюстрации тех же объектов + процедурный glow. Не блокировать разработку.

### 9.3. Motion design

- Rive или Lottie: pair-link, warmth send, perfect-hit.
- Если Rive сложно — Flutter custom painters / RN Reanimated + Skia.

### 9.4. Звук

- UI soft ticks / whooshes (короткие, тихие).
- Heartbeat Tap chart loop.
- Win/lose stingers.
- Ambient room bed (очень тихо, toggle).

Сгенерировать процедурно или из royalty-free; хранить источники в `assets/audio/SOURCES.md`.

---

## 10. Дизайн-система (компоненты)

Создать файл `docs/DESIGN_SYSTEM.md` при реализации. Минимум компонентов:

- `AuroraBackground` (gradient + grain + breathing)
- `BrandMark`
- `PrimaryButton` / `GhostButton` / `DangerButton`
- `PairAvatar`
- `PresenceDot`
- `SectionTitle`
- `GameTile` (интерактивный; единственный “card-like” паттерн каталога)
- `TrackRow`
- `BottomNav`
- `ModalSheet` (кастомная, не дефолтная серая)
- `Toast`
- `CodeInput` (6 символов)
- `ReadyLobby`

Правила spacing: 4/8/12/16/24/32/48.  
Радиусы: 12–20 для интерактива, не stadium-pill everywhere.

---

## 11. Данные и API (логическая модель)

### 11.1. Сущности

- `User { id, displayName, avatarUrl, createdAt, settings }`
- `Pair { id, code, title, memberIds[2], createdAt, moodPreset }`
- `Presence { userId, state, updatedAt }`
- `GameSession { id, pairId, gameType, hostId, state, seed, scores, startedAt, endedAt }`
- `Track { id, pairId, title, artist, coverUrl, sourceType, sourceUrl, addedBy, createdAt }`
- `Playlist { id, pairId, title, trackIds }`
- `Note { id, pairId, text, authorId, createdAt }`
- `Memory { id, pairId, type, payload, createdAt }`
- `DailySpark { date, promptId }`
- `SparkAnswer { pairId, date, userId, text }`

### 11.2. Игровой протокол (общий)

Состояния: `lobby → countdown → playing → round_end → finished`.  
События: `ready`, `unready`, `start`, `input`, `state_sync`, `finish`, `rematch`, `leave`.

Каждая игра описывает свой `input` schema в `docs/GAMES_SPEC.md`.

---

## 12. Безопасность, модерация, сторы

- Google Play Data Safety заполнить честно.
- UGC: жалобы, hide, retention policy.
- Запрещён explicit NSFW, hate, illegal content.
- Хранить минимум PII.
- Секреты только в env, не в клиенте.
- Rate limit на invite/join и presence pings.

---

## 13. Фазы разработки (для автономного Cursor)

### Phase 0 — Foundation (сначала)
- Репо структура, lint, theme tokens, AuroraBackground, typography.
- Splash + Welcome + пустая навигация.
- README с запуском.

### Phase 1 — Pair core
- Auth (можно email magic/password).
- Create/Join pair.
- Home room + presence stub.
- Push warmth MVP.

### Phase 2 — Games MVP
- Lobby framework.
- GAME-01 Signal Draw.
- GAME-02 Heartbeat Tap.
- GAME-06 Truth Or Spark.
- Results + rematch.

### Phase 3 — Music MVP
- Shared tracks CRUD.
- Now playing status.
- Player for uploads / external open.

### Phase 4 — Polish & Store prep
- Ассеты A01–A12, B01–B03 минимум.
- Empty/error/loading.
- Аналитика/краши.
- Скриншоты store, описание, privacy policy page.
- Internal testing build.

### Phase 5 — Expansion
- GAME-03..05.
- Joint listening queue.
- Memories gallery.
- Mood room presets + 3D lantern.
- iOS.

---

## 14. Acceptance Criteria (когда можно сказать “уже круто”)

### 14.1. Must-have для “играбельного продукта”

- [ ] Установка на Android, регистрация, связка двух аккаунтов кодом.
- [ ] Home выглядит брендово в dark-romantic стиле (не шаблон).
- [ ] Можно запустить минимум 3 миниигры на двоих end-to-end.
- [ ] Есть музыкальная библиотека пары (добавление + список + статус).
- [ ] Есть хотя бы 2 ритуала: Daily Spark и Warmth/Candle.
- [ ] Нет критических крашей на основном флоу.
- [ ] Анимации фона/pair-link/переходов присутствуют и не раздражают.
- [ ] Кастомные иконки/обложки игр, не placeholder серые квадраты.
- [ ] Код в git с историей осмысленных коммитов.

### 14.2. Ideal bar (к чему Cursor дотягивает сам)

- [ ] Визуально “это можно снимать в Reels”.
- [ ] Соединение пары ощущается маленькой катсценной магией.
- [ ] Игры понятны за 10 секунд без туториала-простыни.
- [ ] Звуковой слой аккуратный.
- [ ] Пустые состояния красивые.
- [ ] Готовность к internal testing в Play Console.

---

## 15. Метрики успеха (после релиза)

- D1 retention > 35% (ориентир)
- % пар, сыгравших ≥1 игру в первые 24 часа
- Avg session length
- Invites accepted / invites sent
- Tracks added per pair / week
- Crash-free sessions > 99%

---

## 16. Backlog расширений (не забывать, но не мешать MVP)

- Сезонные темы (зима/осень) без смены бренда.
- Совместный фото-полярoid (осторожно с storage).
- Версия “подарочный код” на праздники.
- Private couple stickers.
- Desktop/Steam short narrative companion later (отдельный продукт).
- AI-генерация вопросов дня (с модерацией).
- Wearable pulse sync — later fantasy.

---

## 17. Out of Scope (явно нет в MVP)

- Полноценный мессенджер/звонки.
- Dating для поиска пары.
- Платежи/подписка (можно заложить архитектуру, не включать).
- AR.
- Многопользовательские комнаты >2.
- Точный стриминг всего каталога Spotify без лицензий.

---

## 18. Контент: стартовые промпты Daily Spark / Truth Or Spark

Создать `assets/content/sparks_ru.json` (≥ 60 вопросов), примеры категорий:
- soft memories;
- dreams;
- flirt;
- “выбери за нас”;
- silly;
- gratitude.

Примеры:
1. «Какой наш обычный момент ты бы сохранил в стеклянном шаре?»
2. «Если бы у этого вечера был саундтрек, что бы играло первым?»
3. «Назови черту во мне, которую ты заметил не сразу.»

---

## 19. Store listing (заготовка)

**Название:** Aurora Pair: игры для двоих  
**Short:** Тёмное романтичное пространство для пары — игры, музыка, ритуалы.  
**Long:** (написать при Phase 4; тон поэтичный, без кликбейта)  
**Keywords:** пара, игры для двоих, для влюблённых, couple game, long distance  

Скриншоты обязательные:
1. Home room
2. Pair link cinematic
3. Game catalog
4. In-game (Heartbeat/Draw)
5. Music
6. Ritual candle

---

## 20. План автономной работы агента (операционный)

1. Прочитать это ТЗ.
2. Выбрать стек (Flutter предпочтительно) и зафиксировать в README.
3. Phase 0 → commit/push.
4. Phase 1 → самотест на 2 пользователях (2 эмулятора/профиля) → commit/push.
5. Phase 2 по одной игре: реализация → тест вдвоём → polish juice → commit/push.
6. Параллельно наращивать ассеты из секции 9.
7. Перед остановкой сессии: всегда leave ветку в собираемом состоянии + краткий `PROGRESS.md`.
8. Улучшать UI до соответствия секции 5, даже если фичи уже работают (“работает, но бледно” = не готово).

### 20.1. Файл прогресса

Вести `docs/PROGRESS.md`:
- что сделано;
- что дальше;
- известные баги;
- какие ассеты ещё missing.

---

## 21. Открытые решения (можно зафиксировать позже без блокировки)

| Тема | Временное решение по умолчанию |
|------|--------------------------------|
| Финальный нейминг | AURORA PAIR |
| Стек клиента | Flutter |
| Backend | Firebase |
| Подписка | нет в MVP |
| Стриминг музыки | links + uploads |
| 3D на Home | glTF если perf ок, иначе baked 2D |
| Возрастной рейтинг | 16+ |

---

## 22. Changelog документа

### 1.0.0-draft
- Первичное большое ТЗ: продукт, UX, игры, музыка, ассеты, фазы, acceptance, правила для Cursor Agent.

---

## 23. Приложение A — Чеклист “это не дефолтная приложуха”

Пройти глазами перед каждым крупным push:

1. Есть ли бренд-герой, а не только текст в аппбаре?
2. Фон имеет глубину (градиент/grain/свет), а не `#000` плоско?
3. Шрифты выразительные, не системный стек?
4. Нет розово-милых клипартов и нет purple neon AI look?
5. Карточек не слишком много? Home не dashboard?
6. Есть motion, который создаёт присутствие?
7. Иконки кастомные?
8. Пустые состояния не стыдные?
9. Микрокопирайт звучит по-человечески?
10. Хочется показать скрин друга́м?

Если ≥2 ответа “нет” — дорабатывать визуал, не переходить к новым крупным фичам.

---

**Конец ТЗ v1.0.0-draft.**  
Дальше: человек уточняет/режет/добавляет → агент реализует по фазам до Ideal bar.
