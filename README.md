# Physical Password Manager

### Entwicklungsdokumentaion
- Erstellen des Projektes mit Compose Multiplatform
- Datenmodell Implementieren
   - Ordner
   - Eintrag
   - Example Data
- interactive GUI
- icon downloader 
- scene switch logic with an enum
- basic login scene
- image manager (don't repreat icon download from a site, when the image is already downloaded) ...
  - problem with webp and icon format
  - solution: com.twelvemonkeys.imageio libery
- ... and show website-Icons, but they don't have an equal size
- well, now they have one, but the download actually only works for 2 sides: chefkoch.de and chat.openai.com (from the sides, which I tested). I think actually it is highly unlikely that I put the two websites in my example data in the root folder, which works.
- Cause and solution of the mentioned Problem: I forgot about relative paths in websites, so if I got something like ``./icon.ico`` as the icon address, I tried to call ``./icon.ico`` and not ``https://website.de/iocn.ico``. The fix was that I added the domain when the path is relative.
- Problem with svg-images: When the content type of the svg is ``text/html``(https://vplan.plus/favicon.svg) and not something like ``image/svg+xml``(https://www.jetbrains.com/icon.svg?r=1234), it can't read the svg
- 