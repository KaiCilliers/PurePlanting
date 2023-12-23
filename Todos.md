# TODOs

## Important
- [x] Logic to mark notifications as read
- [x] System notification deeplink on tap
- [ ] (Pending...) Fix bug where workers only run when app is foregrounded
- [ ] (Pending...) Fix bug where notification is received every 15min
- [x] Snackbar emitter
- [x] Animations for dialog appearance
- [x] List content update with animation
- [x] Detail screen description content spacing
- [x] Water soon bug where all unwatered plants are sent in notification
- [ ] Potential bug based on resolution of above - Bug where workmanager is triggered for water soon immediately when app is launched
- [x] Unwater soon not working
- [ ] Incorrect notification message for single plants
- [ ] Plant list empty state per filter
- [ ] Notification List empty state
- [ ] Home with correct background
- [ ] Override toolbar safe area

## Not important

### Build Config
- [ ] Be able to run all tests with a single command
- [ ] Common Android and Kotlin Gradle files
- [ ] Clean up Gradle files and only use dependencies that are required by module
- [ ] App Icon per build config
- [ ] App name per build config
- [ ] R8 file per module
- [ ] Screen with info of app version and name

### Logic
- [ ] Worker to clear temp files
- [ ] Optimize usage of images

### Behaviour
- [ ] Logic to prevent the user from navigating away from Add/Edit screen when they have made changes (prompt them with a confirmation dialog)
- [ ] Navigation animation (replace or back, or front)

### Minor
- [ ] Remove hardcoded DP values
- [ ] Live template to create UI compose components with a preview (private)
- [ ] See what the data_extraction_rules.xml is used for
- [ ] Use string resources
- [ ] Setup MaterialTheme (colors, typography)
