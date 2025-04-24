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
- ... and show Webside-Icons, but they don't have an equal size
- well, now they have one, but the download actually only works for 2 sides: chefkoch.de and chat.openai.com (from the sides, which I tested). I think actually it is highly unlikely that I put the two websites in my example data in the root folder, which works.
- 