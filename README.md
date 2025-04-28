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
- well, now they have one, but the download actually only works for two sides: chefkoch.de and chat.openai.com (from the sides, which I tested). I think actually it is highly unlikely that I put the two websites in my example data in the root folder, which works.
- Cause and solution of the mentioned Problem: I forgot about relative paths in websites, so if I got something like ``./icon.ico`` as the icon address, I tried to call ``./icon.ico`` and not ``https://website.de/iocn.ico``. The fix was that I added the domain when the path is relative.
- Problem with svg-images: When the content type of the svg is ``text/html``(https://vplan.plus/favicon.svg) and not something like ``image/svg+xml``(https://www.jetbrains.com/icon.svg?r=1234), it can't read the svg
- Fix: Some sides redirect ``/./icon.ico`` to ``/icon.ico`` not with the http-headers but with a script or something like ``<meta http-equiv="refresh" content="0; url=http://example.com/" />`` inside the HTML document. KTor can't comprehensibly follow these types of redirections. Solution: Remove all ``./`` patterns from the url.
- Rearrange data structure: the sum of all evils is constant, previously I have worked in two lanes in my pwdList class: I had a ``data`` object, which contained my data that was parsed from the JSON String and a ``entries`` object, which was a ``MutableState<List<Entrie>>``, so every time, I replaced the list in the ``entries`` object, the gui was updated. The problem was that the update of the ``entries`` object didn't update the ``data`` object, because I had to override the object to update the UI, because the attributes weren't observed. In the new version, I have two folder and entry objects: one for parsing from the JSON string and one where all children are ``MutableState``s or ``SnapshotStateList``, so everything is observed. This has the downside that the data must be mapped from ``SerializedEntry`` and ``SerializedFolder`` to the normal ``Entry`` and ``Folder``, because the Kotlin serialization doesn't support the ``MutableState`` and ``SnapshotStateList``, but I only have to do this once's and don't have to mess with two independent objects that have to be synchronized.
- The GUI was updated with three buttons: add, edit and delete for manipulating the entries. The Add button is already created an ``EntryManager`` object, which has a form to add a new entry. (multiple ``EntryManager``s are possible)
- Add a checkbox group, where you can select one at a time, to edit and delete entries via the variable ``selectedElementIndex``
- Restructured scene and window logic: now there is only one window where everything happens.
- SerialPortIO for making request to the Pi
- connected the get getExampleData() with SerialPortIO