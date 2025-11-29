package com.example.proyectodd.view

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.proyectodd.model.Usuario
import com.example.proyectodd.viewmodel.PersonajeViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Bg = Color(0xFF000000)
private val Card = Color(0xFF292929)
private val Line = Color(0xFF7F1D1D)
private val Hint = Color(0xFFFFFFFF)

@Composable
fun CardDndForm(
    usuario: Usuario,
    vmExternal: PersonajeViewModel? = null,
    onCancel: () -> Unit = {},
    onSaved: () -> Unit = {}
) {
    val context = LocalContext.current

    // ✔ USAR SIEMPRE EL VIEWMODEL ENTREGADO DESDE NavegacionAuth
    val vm = vmExternal
        ?: error("PersonajeViewModel no fue entregado desde NavegacionAuth")

    val estado by vm.estado.collectAsState()

    var characterName by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("") }
    var race by remember { mutableStateOf("") }
    var clazz by remember { mutableStateOf("") }
    var background by remember { mutableStateOf("") }
    var alignment by remember { mutableStateOf("") }

    var str by rememberSaveable { mutableStateOf("") }
    var dex by rememberSaveable { mutableStateOf("") }
    var con by rememberSaveable { mutableStateOf("") }
    var intg by rememberSaveable { mutableStateOf("") }
    var wis by rememberSaveable { mutableStateOf("") }
    var cha by rememberSaveable { mutableStateOf("") }

    var initiative by rememberSaveable { mutableStateOf("") }
    var ac by rememberSaveable { mutableStateOf("") }
    var passive by rememberSaveable { mutableStateOf("") }
    var hp by rememberSaveable { mutableStateOf("") }
    var saveDC by rememberSaveable { mutableStateOf("") }
    var speed by rememberSaveable { mutableStateOf("") }

    var playerName by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(estado.id) {
        characterName = estado.nombre
        level = estado.nivel.takeIf { it != 0 }?.toString() ?: ""
        race = estado.raza
        clazz = estado.clase
        background = estado.trasfondo
        alignment = estado.alineamiento

        str = estado.str.takeIf { it != 0 }?.toString() ?: ""
        dex = estado.dex.takeIf { it != 0 }?.toString() ?: ""
        con = estado.con.takeIf { it != 0 }?.toString() ?: ""
        intg = estado.intg.takeIf { it != 0 }?.toString() ?: ""
        wis = estado.sab.takeIf { it != 0 }?.toString() ?: ""
        cha = estado.car.takeIf { it != 0 }?.toString() ?: ""

        initiative = estado.iniciativa.takeIf { it != 0 }?.toString() ?: ""
        ac = estado.ac.takeIf { it != 0 }?.toString() ?: ""
        passive = estado.percepcionPasiva.takeIf { it != 0 }?.toString() ?: ""
        hp = estado.hp.takeIf { it != 0 }?.toString() ?: ""
        saveDC = estado.saveDC.takeIf { it != 0 }?.toString() ?: ""
        speed = estado.velocidad.takeIf { it != 0 }?.toString() ?: ""

        playerName = usuario.nombre
    }

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

            // -----------------------
            // HEADER: NAME + LEVEL
            // -----------------------
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                BannerField1(
                    label = "CHARACTER NAME",
                    value = characterName,
                    onValueChange = {
                        characterName = it
                        vm.setNombre(it)
                    }
                )
                Spacer(Modifier.width(1.dp))
                SmallBadge(
                    label = "LEVEL",
                    value = level,
                    onValueChange = {
                        level = it.filter(Char::isDigit).take(2)
                        vm.setNivel(level)
                    },
                    width = 80.dp
                )
                Spacer(Modifier.width(1.dp))
                PortraitPicker(
                    portraitUri = estado.portraitUri,
                    onPicked = { uri -> vm.setPortrait(uri) }
                )
            }

            // -----------------------
            // BASIC FIELDS
            // -----------------------
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BannerField(
                    label = "RACE",
                    value = race,
                    onValueChange = { race = it; vm.setRaza(it) },
                    modifier = Modifier.weight(1f)
                )
                BannerField(
                    label = "CLASS",
                    value = clazz,
                    onValueChange = { clazz = it; vm.setClase(it) },
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
                    onValueChange = { background = it; vm.setTrasfondo(it) },
                    modifier = Modifier.weight(1f)
                )
                BannerField(
                    label = "ALIGNMENT",
                    value = alignment,
                    onValueChange = { alignment = it; vm.setAlineamiento(it) },
                    modifier = Modifier.weight(1f)
                )
            }


            Spacer(Modifier.height(14.dp))

            // -----------------------
            // STATS
            // -----------------------
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AbilityBox("Str", str) { str = it.digits2(); vm.setStr(str) }
                AbilityBox("Dex", dex) { dex = it.digits2(); vm.setDex(dex) }
                AbilityBox("Con", con) { con = it.digits2(); vm.setCon(con) }
                AbilityBox("Int", intg) { intg = it.digits2(); vm.setIntg(intg) }
                AbilityBox("Wis", wis) { wis = it.digits2(); vm.setSab(wis) }
                AbilityBox("Cha", cha) { cha = it.digits2(); vm.setCar(cha) }
            }

            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .weight(0.35f)
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShieldBox("AC", ac) { ac = it.digits2(); vm.setAC(ac) }
                    SquareBox("P.PERCEPTION", passive) { passive = it.digits2(); vm.setPercepcionPasiva(passive) }
                }
                Column(
                    Modifier.weight(0.3f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SquareBox("INITIATIVE", initiative) { initiative = it.signed2(); vm.setIniciativa(initiative) }
                    SquareBox("SPELL SAVE DC", saveDC) { saveDC = it.digits2(); vm.setSaveDC(saveDC) }
                }
                Column(
                    Modifier.weight(0.3f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SquareBox("SPEED", speed) { speed = it.digits3(); vm.setVelocidad(speed) }
                    ShieldBox("HIT POINTS", hp) { hp = it.digits3(); vm.setHP(hp) }
                }
            }

            Spacer(Modifier.height(14.dp))

            // -----------------------
            // PLAYER NAME (usuario)
            // -----------------------
            BannerField(
                label = "PLAYER NAME",
                value = playerName,
                onValueChange = { playerName = it; vm.setPlayerName(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onCancel) { Text("Cancelar") }
                Button(onClick = { vm.guardar(onSaved) }) { Text("Guardar", color = Color.White) }
            }
        }
    }
}

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
            modifier = Modifier.width(55.dp),
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

@Composable
private fun PortraitPicker(
    portraitUri: String?,
    onPicked: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

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
            if (portraitUri.isNullOrBlank()) {
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
        Row (modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { requestCameraPermission.launch(Manifest.permission.CAMERA) }) {
                Text("Cámara", color = Color.White)
            }
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile("JPEG_${time}_", ".jpg", storageDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

private fun String.digits2() = filter(Char::isDigit).take(2)
private fun String.digits3() = filter(Char::isDigit).take(3)
private fun String.signed2(): String {
    if (isEmpty()) return this
    val sign = if (first() == '-' || first() == '+') first().toString() else ""
    val body = dropWhile { it == '-' || it == '+' }.filter(Char::isDigit).take(2)
    return sign + body
}
