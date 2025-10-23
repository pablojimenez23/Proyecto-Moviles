package com.example.proyectodd.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.proyectodd.viewmodel.PersonajeViewModel
import com.example.proyectodd.viewmodel.PersonajeViewModelFactory
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import android.os.Environment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.File
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectodd.data.local.database.AppDatabase
import com.example.proyectodd.data.repository.PersonajeRepository
import coil.compose.rememberAsyncImagePainter
import com.example.proyectodd.data.model.Usuario


/* Estilo simple blanco/gris con bordes finos */
private val Bg = Color(0xFFF7F7F7)
private val Card = Color(0xFFFFFFFF)
private val Line = Color(0xFF2F2F2F)
private val Hint = Color(0xFF666666)

/* ============== PREVIEW ============== */
/* @Preview(showBackground = true, backgroundColor = 0xFFF7F7F7, widthDp = 392, heightDp = 820)
@Composable
fun CardDndFormPreview() { CardDndForm() }
*/
/* ============== SCREEN ============== */
@Composable
fun CardDndForm(usuario: Usuario) {
    // Encabezado
    var characterName by rememberSaveable { mutableStateOf("") }
    var level by rememberSaveable { mutableStateOf("") }
    var race by rememberSaveable { mutableStateOf("") }
    var clazz by rememberSaveable { mutableStateOf("") }
    var background by rememberSaveable { mutableStateOf("") }
    var alignment by rememberSaveable { mutableStateOf("") }


    // Habilidades
    var str by rememberSaveable { mutableStateOf("") }
    var dex by rememberSaveable { mutableStateOf("") }
    var con by rememberSaveable { mutableStateOf("") }
    var intg by rememberSaveable { mutableStateOf("") }
    var wis by rememberSaveable { mutableStateOf("") }
    var cha by rememberSaveable { mutableStateOf("") }

    // Centro / derecha
    var initiative by rememberSaveable { mutableStateOf("") }
    var ac by rememberSaveable { mutableStateOf("") }
    var passive by rememberSaveable { mutableStateOf("") }
    var hp by rememberSaveable { mutableStateOf("") }
    var saveDC by rememberSaveable { mutableStateOf("") }
    var speed by rememberSaveable { mutableStateOf("") }

    // Pie
    var playerName by rememberSaveable { mutableStateOf("") }

    // --- Integración con ViewModel + Room ---
    val context = LocalContext.current
    val db = remember { AppDatabase.obtenerBaseDatos(context) }
    val repo = remember { PersonajeRepository(db.personajeDao()) }
    val factory = remember { PersonajeViewModelFactory(repo) }
    val vm: PersonajeViewModel = viewModel(factory = factory)
    val estado by vm.estado.collectAsState()

    // Sincroniza los campos visibles con la base de datos
    LaunchedEffect(estado) {
        characterName = estado.nombre
        level = estado.nivel.toString()
        race = estado.raza
        clazz = estado.clase
        background = estado.trasfondo
        alignment = estado.alineamiento
        str = estado.str.toString()
        dex = estado.dex.toString()
        con = estado.con.toString()
        intg = estado.intg.toString()
        wis = estado.sab.toString()
        cha = estado.car.toString()
        initiative = estado.iniciativa.toString()
        ac = estado.ac.toString()
        passive = estado.percepcionPasiva.toString()
        hp = estado.hp.toString()
        saveDC = estado.saveDC.toString()
        speed = estado.velocidad.toString()
        playerName = usuario.nombre
    ; vm.setNombre(characterName); vm.guardar() }


    Box(
        Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Card)
                .border(1.dp, Line, RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {

            // Nombre + Nivel
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                BannerField1(
                    label = "CHARACTER NAME",
                    value = characterName,
                    onValueChange = { characterName = it ; vm.setNombre(characterName); vm.guardar() }


                )
                Spacer(Modifier.width(1.dp))
                SmallBadge(
                    label = "LEVEL",
                    value = level,
                    onValueChange = { level = it.filter(Char::isDigit).take(2) ; vm.setNivel(level); vm.guardar() },
                    width = 80.dp
                )
                Spacer(Modifier.width(1.dp))
                Box(
                    Modifier
                        .width(97.dp)
                        .height(97.dp)
                        .clip(RoundedCornerShape(10.dp))

                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Portrait",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                }
            }



            // RACE / CLASS / BACKGROUND / ALIGNMENT
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BannerField(
                    label = "RACE",
                    value = race,
                    onValueChange = { race = it ; vm.setRaza(race); vm.guardar() },
                    modifier = Modifier.weight(1f)
                )
                BannerField(
                    label = "CLASS",
                    value = clazz,
                    onValueChange = { clazz = it ; vm.setClase(clazz); vm.guardar() },
                    modifier = Modifier.weight(1f)
                )
            }



            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BannerField(
                    label = "BACKGROUND",
                    value = background,
                    onValueChange = { background = it ; vm.setTrasfondo(background); vm.guardar() },
                    modifier = Modifier.weight(1f)
                )
                BannerField(
                    label = "ALIGNMENT",
                    value = alignment,
                    onValueChange = { alignment = it ; vm.setAlineamiento(alignment); vm.guardar() },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AbilityBox("Str", str) { str = it.digits2() ; vm.setStr(str); vm.guardar() }
                AbilityBox("Dex", dex) { dex = it.digits2() ; vm.setDex(dex); vm.guardar() }
                AbilityBox("Con", con) { con = it.digits2() ; vm.setCon(con); vm.guardar() }
                AbilityBox("Int", intg){ intg = it.digits2() ; vm.setIntg(intg); vm.guardar() }
                AbilityBox("Wis", wis) { wis = it.digits2() ; vm.setSab(wis); vm.guardar() }
                AbilityBox("Cha", cha) { cha = it.digits2() ; vm.setCar(cha); vm.guardar() }
            }
            Spacer(Modifier.height(14.dp))
            // Cuerpo con 3 columnas: Izq (6 habilidades), Centro (Initiative/Passive/Save/Speed), Derecha (AC/HP)
            Row(Modifier.fillMaxWidth()) {

                // izquierda
                Column(
                    Modifier
                        .weight(0.35f)
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShieldBox("AC", ac, { ac = it.digits2() ; vm.setAC(ac); vm.guardar() })
                    SquareBox("P.PERCEPTION", passive, { passive = it.digits2() ; vm.setPercepcionPasiva(passive); vm.guardar() })


                }

                // medio
                Column(
                    Modifier.weight(0.3f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SquareBox("INITIATIVE", initiative, { initiative = it.signed2() ; vm.setIniciativa(initiative); vm.guardar() })
                    SquareBox("SPELL SAVE DC", saveDC, { saveDC = it.digits2() ; vm.setSaveDC(saveDC); vm.guardar() })
                }
                //derecha
                Column(
                    Modifier.weight(0.3f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SquareBox("SPEED", speed, { speed = it.digits3() ; vm.setVelocidad(speed); vm.guardar() })
                    ShieldBox("HIT POINTS", hp, { hp = it.digits3() ; vm.setHP(hp); vm.guardar() })
                }
            }

            Spacer(Modifier.height(14.dp))
            BannerField(
                label = "PLAYER NAME",
                value = playerName,
                onValueChange = { playerName = it ; vm.setPlayerName(playerName); vm.guardar() },
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

/* =================== COMPONENTES =================== */
@Composable
private fun AbilityBox(label: String, score: String, onChange: (String) -> Unit) {
    val value = score.toIntOrNull() ?: 10
    val mod = ((value - 10) / 2.0).toInt()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Hint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Box(
            Modifier
                .width(54.dp)
                .height(54.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = score,
                onValueChange = onChange,
                singleLine = true,

                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Line,
                    unfocusedBorderColor = Line.copy(alpha = .5f),
                    cursorColor = Line
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            )
        }
        Spacer(Modifier.height(4.dp))
        Text(if (mod >= 0) "+$mod" else "$mod", color = Hint, fontSize = 12.sp)
    }
}
@Composable
private fun BannerField1(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clip(RoundedCornerShape(10.dp))

            .padding(10.dp)
    ) {
        Text(label, color = Hint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .width(150.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Line,
                unfocusedBorderColor = Line.copy(alpha = .5f),
                cursorColor = Line
            )
        )
    }
}
@Composable
private fun BannerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clip(RoundedCornerShape(10.dp))

            .padding(10.dp)
    ) {
        Text(label, color = Hint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier

                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Line,
                unfocusedBorderColor = Line.copy(alpha = .5f),
                cursorColor = Line
            )
        )
    }
}

@Composable
private fun SmallBadge(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    width: Dp
) {
    Column(
        Modifier
            .width(width)
            .clip(RoundedCornerShape(10.dp))

            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = Hint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(55.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Line,
                unfocusedBorderColor = Line.copy(alpha = .5f),
                cursorColor = Line
            )
        )
    }
}

@Composable
private fun AbilityRow(
    badge: String,
    value: String,
    onChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // círculo de la izquierda (S/D/C/I/W/Ch)
        Box(
            Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(1.dp, Line, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(badge, color = Line, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(Modifier.width(8.dp))
        // caja alargada (valor)
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Line,
                unfocusedBorderColor = Line.copy(alpha = .5f),
                cursorColor = Line
            )
        )
    }
}

@Composable
private fun SquareBox(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(10.dp))

            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = Hint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Line,
                unfocusedBorderColor = Line.copy(alpha = .5f),
                cursorColor = Line
            )
        )
    }
}

@Composable
private fun ShieldBox(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))

            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = Hint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Line,
                unfocusedBorderColor = Line.copy(alpha = .5f),
                cursorColor = Line
            )
        )
    }
}

/* ============== Helpers de entrada ============== */
private fun String.digits2() = filter(Char::isDigit).take(2)
private fun String.digits3() = filter(Char::isDigit).take(3)
private fun String.signed2(): String {
    if (isEmpty()) return this
    val sign = if (first() == '-' || first() == '+') first().toString() else ""
    val body = dropWhile { it == '-' || it == '+' }.filter(Char::isDigit).take(2)
    return sign + body
}



@Composable
private fun PortraitPicker(
    portraitUri: String?,
    onPicked: (android.net.Uri?) -> Unit
) {
    val context = LocalContext.current
    var cameraUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val pickFromGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> onPicked(uri) }

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (success) onPicked(cameraUri) }

    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            cameraUri = uri
            takePicture.launch(uri)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(97.dp)
            .height(130.dp)
    ) {
        val painter = rememberAsyncImagePainter(model = portraitUri)

        Box(
            Modifier
                .width(97.dp)
                .height(97.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE0E0E0))
                .clickable { pickFromGallery.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (portraitUri == null) {
                Text(
                    text = "Portrait",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Image(
                    painter = painter,
                    contentDescription = "Portrait",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        Button(onClick = { requestCameraPermission.launch(Manifest.permission.CAMERA) }) {
            Text("Cámara")
        }
    }
}


private fun createImageUri(context: android.content.Context): android.net.Uri {
    val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile("JPEG_${time}_", ".jpg", storageDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}
