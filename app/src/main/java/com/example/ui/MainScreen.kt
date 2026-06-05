package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*
import com.example.viewmodel.RollResolution
import com.example.viewmodel.ShatterpointViewModel
import com.example.viewmodel.SimulationSummary
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ShatterpointViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val characters by viewModel.characters.collectAsStateWithLifecycle()

    // Default selection on first launch
    LaunchedEffect(characters) {
        if (characters.isNotEmpty()) {
            if (viewModel.selectedAttackerId.value == null) {
                viewModel.selectedAttackerId.value = characters.firstOrNull { it.id == "anakin_skywalker" }?.id ?: characters.firstOrNull()?.id
            }
            if (viewModel.selectedDefenderId.value == null) {
                viewModel.selectedDefenderId.value = characters.firstOrNull { it.id == "darth_maul" }?.id ?: characters.getOrNull(1)?.id
            }
        }
    }

    val attacker = characters.find { it.id == viewModel.selectedAttackerId.value }
    val defender = characters.find { it.id == viewModel.selectedDefenderId.value }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = DeepCharcoal,
                contentColor = Color.White,
                modifier = Modifier.navigationBarsPadding()
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Tirador") },
                    label = { Text("Tirador", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = ShatterpointAmber,
                        indicatorColor = ShatterpointAmber,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_roller")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Caja Base") },
                    label = { Text("Personajes", fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = ShatterpointAmber,
                        indicatorColor = ShatterpointAmber,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_characters")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SpaceBlack)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> TabRoller(viewModel = viewModel, characters = characters, attacker = attacker, defender = defender)
                1 -> TabCharacters(viewModel = viewModel, characters = characters)
            }
        }
    }
}

// TAB 1: COMBAT ROLLER
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TabRoller(
    viewModel: ShatterpointViewModel,
    characters: List<CharacterEntity>,
    attacker: CharacterEntity?,
    defender: CharacterEntity?
) {
    val attackerStanceIdx by viewModel.selectedAttackerStanceIndex.collectAsStateWithLifecycle()
    val defenderStanceIdx by viewModel.selectedDefenderStanceIndex.collectAsStateWithLifecycle()
    val attackCount by viewModel.attackDiceCount.collectAsStateWithLifecycle()
    val defenseCount by viewModel.defenseDiceCount.collectAsStateWithLifecycle()

    val currentAtkRoll by viewModel.currentAttackRoll.collectAsStateWithLifecycle()
    val currentDefRoll by viewModel.currentDefenseRoll.collectAsStateWithLifecycle()

    val attackerNotes by viewModel.attackerNotes.collectAsStateWithLifecycle()
    val defenderNotes by viewModel.defenderNotes.collectAsStateWithLifecycle()
    val roundNotes by viewModel.roundNotes.collectAsStateWithLifecycle()

    var editAtkDieIndex by remember { mutableStateOf<Int?>(null) }
    var editDefDieIndex by remember { mutableStateOf<Int?>(null) }
    var showResolutionPopup by remember { mutableStateOf(false) }

    val resolution = viewModel.calculateCurrentOutcome(
        attacker = attacker,
        defender = defender,
        attackerStanceIdx = attackerStanceIdx,
        defenderStanceIdx = defenderStanceIdx,
        atkRoll = currentAtkRoll,
        defRoll = currentDefRoll
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("tab_roller_root")
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "COMBAT ROLLER & COMPANION",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = ShatterpointAmber,
                letterSpacing = 1.sp
            )
        }

        // COMBINED DICE QUANTITY SELECTOR & ROLL BUTTON ROW
        item {
            SectionCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dice quantity adjustment (balanced weights to give more room to the launch button)
                    Column(
                        modifier = Modifier.weight(1.1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "RESERVA DE DADOS",
                            style = MaterialTheme.typography.labelSmall,
                            color = ShatterpointAmber,
                            fontWeight = FontWeight.ExtraBold
                        )
                        
                        // Attack choice (replaces "Ataque:" with ⚔ Icon badge)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFEF5350).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFFEF5350).copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("⚔", fontSize = 20.sp, color = Color(0xFFEF5350), fontWeight = FontWeight.Bold)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                IconButton(
                                    onClick = { if (attackCount > 0) viewModel.attackDiceCount.value = attackCount - 1 },
                                    modifier = Modifier.size(44.dp),
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = LightCharcoal)
                                ) {
                                    Text("-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(LightCharcoal, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = attackCount.toString(),
                                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                IconButton(
                                    onClick = { if (attackCount < 20) viewModel.attackDiceCount.value = attackCount + 1 },
                                    modifier = Modifier.size(44.dp),
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = LightCharcoal)
                                ) {
                                    Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                                }
                            }
                        }

                        // Defense choice (replaces "Defensa:" with 🛡 Icon badge)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFF2196F3).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🛡", fontSize = 20.sp, color = Color(0xFF64B5F6), fontWeight = FontWeight.Bold)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                IconButton(
                                    onClick = { if (defenseCount > 0) viewModel.defenseDiceCount.value = defenseCount - 1 },
                                    modifier = Modifier.size(44.dp),
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = LightCharcoal)
                                ) {
                                    Text("-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(LightCharcoal, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = defenseCount.toString(),
                                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                IconButton(
                                    onClick = { if (defenseCount < 20) viewModel.defenseDiceCount.value = defenseCount + 1 },
                                    modifier = Modifier.size(44.dp),
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = LightCharcoal)
                                ) {
                                    Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                                }
                            }
                        }
                    }

                    // Elegant vertical divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(90.dp)
                            .background(LightCharcoal)
                    )

                    // Roller button on the other side, balanced nicely and wider
                    Column(
                        modifier = Modifier.weight(0.9f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { viewModel.rollDice() },
                            colors = ButtonDefaults.buttonColors(containerColor = ShatterpointAmber),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("roll_dice_button"),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Lanzar",
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "LANZAR",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // DICE ROLLS DISPLAY (Tapping die cycles values!)
        if (currentAtkRoll.isNotEmpty() || currentDefRoll.isNotEmpty()) {
            item {
                SectionCard(backgroundColor = DeepCharcoal) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Resultados de Dados Obtenidos",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Toca para cambiar cara",
                                style = MaterialTheme.typography.labelSmall,
                                color = ShatterpointAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        // Attack dice list
                        if (currentAtkRoll.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Ataque",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    currentAtkRoll.forEachIndexed { index, face ->
                                        AttackDieWidget(
                                            face = face,
                                            onClick = { editAtkDieIndex = index }
                                        )
                                    }
                                }
                            }
                        }

                        // Defense dice list
                        if (currentDefRoll.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Defensa",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    currentDefRoll.forEachIndexed { index, face ->
                                        DefenseDieWidget(
                                            face = face,
                                            onClick = { editDefDieIndex = index }
                                        )
                                    }
                                }
                            }
                        }

                        // 💥 POPUP BUTTON 💥
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { showResolutionPopup = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ShatterpointAmber),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("btn_show_resolution_popup")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "💥 VER RESOLUCIÓN DE COMBATE",
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        // Space/Padding at end of list
        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }

    // Pop-up dialog showing only final net successes, which recalculates automatically when changing dice
    if (showResolutionPopup) {
        AlertDialog(
            onDismissRequest = { showResolutionPopup = false },
            containerColor = DeepCharcoal,
            title = {
                Text(
                    text = "RESOLUCIÓN DE COMBATE",
                    color = ShatterpointAmber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "ÉXITOS NETOS FINALES",
                        style = MaterialTheme.typography.labelMedium,
                        color = ShatterpointAmber,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        textAlign = TextAlign.Center
                    )
                    
                    // Huge circular successes indicator
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(ShatterpointAmber, Color(0xFFFFA000))
                                )
                            )
                            .border(3.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${resolution.totalSuccesses}",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 54.sp,
                                color = Color.Black
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${resolution.netStrikes} Impacto(s) ⚔  +  ${resolution.netCrits} Crítico(s) 🌟",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                        
                        if (resolution.blockedStrikes > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "(${resolution.blockedStrikes} impactos bloqueados por defensa 🛡)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
            },
            confirmButton = {
                TextButton(onClick = { showResolutionPopup = false }) {
                    Text("ENTENDIDO", color = ShatterpointAmber, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Edit individual Attack Die face dialog
    if (editAtkDieIndex != null) {
        val index = editAtkDieIndex!!
        val currentFace = currentAtkRoll.getOrNull(index)
        AlertDialog(
            onDismissRequest = { editAtkDieIndex = null },
            title = {
                Text(
                    text = "Seleccionar Cara de Ataque",
                    color = ShatterpointAmber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            containerColor = DeepCharcoal,
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Selecciona la cara para el dado de ataque #${index + 1}:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    AttackDiceFace.values().forEach { face ->
                        val isSelected = currentFace == face
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) LightCharcoal else Color.Transparent)
                                .clickable {
                                    viewModel.setAttackDie(index, face)
                                    editAtkDieIndex = null
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AttackDieWidget(
                                face = face,
                                onClick = {
                                    viewModel.setAttackDie(index, face)
                                    editAtkDieIndex = null
                                }
                            )
                            val label = when (face) {
                                AttackDiceFace.CRITICAL -> "Crítico (Crit)"
                                AttackDiceFace.STRIKE -> "Impacto (Strike)"
                                AttackDiceFace.EXPERTISE -> "Pericia (Eye)"
                                AttackDiceFace.FAILURE -> "Fallo (Failure)"
                            }
                            Text(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) ShatterpointAmber else Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { editAtkDieIndex = null }) {
                    Text("Cancelar", color = ShatterpointAmber)
                }
            }
        )
    }

    // Edit individual Defense Die face dialog
    if (editDefDieIndex != null) {
        val index = editDefDieIndex!!
        val currentFace = currentDefRoll.getOrNull(index)
        AlertDialog(
            onDismissRequest = { editDefDieIndex = null },
            title = {
                Text(
                    text = "Seleccionar Cara de Defensa",
                    color = ShatterpointAmber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            containerColor = DeepCharcoal,
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Selecciona la cara para el dado de defensa #${index + 1}:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    DefenseDiceFace.values().forEach { face ->
                        val isSelected = currentFace == face
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) LightCharcoal else Color.Transparent)
                                .clickable {
                                    viewModel.setDefenseDie(index, face)
                                    editDefDieIndex = null
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DefenseDieWidget(
                                face = face,
                                onClick = {
                                    viewModel.setDefenseDie(index, face)
                                    editDefDieIndex = null
                                }
                            )
                            val label = when (face) {
                                DefenseDiceFace.BLOCK -> "Bloqueo (Block)"
                                DefenseDiceFace.EXPERTISE -> "Pericia (Eye)"
                                DefenseDiceFace.FAILURE -> "Fallo (Failure)"
                            }
                            Text(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) ShatterpointAmber else Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { editDefDieIndex = null }) {
                    Text("Cancelar", color = ShatterpointAmber)
                }
            }
        )
    }
}

// TAB 2: CAJA BASE PROFILES (PRELOADED DATABASE)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TabCharacters(
    viewModel: ShatterpointViewModel,
    characters: List<CharacterEntity>
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeFilter by remember { mutableStateOf<String?>(null) } // "Principal", "Secundario", "Apoyo"
    
    // Custom Creation Form state
    var showCreateForm by remember { mutableStateOf(false) }
    var newCharName by remember { mutableStateOf("") }
    var newCharTitle by remember { mutableStateOf("") }
    var newCharType by remember { mutableStateOf("Principal") }
    var newCharFaction by remember { mutableStateOf("Personalizado") }
    var newCharStamina by remember { mutableStateOf("9") }
    var newCharDurability by remember { mutableStateOf("2") }
    var newCharForce by remember { mutableStateOf("4") }

    val filteredCharacters = characters.filter { char ->
        val matchesSearch = char.name.contains(searchQuery, ignoreCase = true) || char.title.contains(searchQuery, ignoreCase = true) || char.faction.contains(searchQuery, ignoreCase = true)
        val matchesType = selectedTypeFilter == null || char.type == selectedTypeFilter
        matchesSearch && matchesType
    }

    val activeInspect by viewModel.activeInspectCharacter.collectAsStateWithLifecycle()

    if (activeInspect != null) {
        // Detailed Inspect Screen overlay/view
        val character = activeInspect!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.activeInspectCharacter.value = null },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = LightCharcoal)
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Volver", tint = Color.White)
                }
                Text("Carta de Perfil", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ShatterpointAmber)
                Spacer(modifier = Modifier.width(48.dp)) // symmetry spacer
            }

            // Character Header Card
            SectionCard(backgroundColor = DeepCharcoal) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = character.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = ShatterpointAmber,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text(character.type, color = Color.White) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = LightCharcoal)
                        )
                        SuggestionChip(
                            onClick = {},
                            label = { Text(character.faction, color = Color.White) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = LightCharcoal)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = LightCharcoal)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Character Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Resistencia (Stamina)", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                            Text(character.stamina.toString(), style = MaterialTheme.typography.headlineMedium, color = SeparatistGreen, fontWeight = FontWeight.Black)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Durabilidad", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                            Text(character.durability.toString(), style = MaterialTheme.typography.headlineMedium, color = ImperialRed, fontWeight = FontWeight.Black)
                        }
                        if (character.type == "Principal") {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Ptos. Fuerza", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                                Text(character.forcePoints.toString(), style = MaterialTheme.typography.headlineMedium, color = AllianceCyan, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            // Stances Section
            Text("Estilo de Combate (Stance) & Tablas de Pericia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            character.stances.forEach { stance ->
                SectionCard {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(stance.name, style = MaterialTheme.typography.titleMedium, color = ShatterpointAmber, fontWeight = FontWeight.Bold)
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Cuerpo a Cuerpo", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                                Text("${stance.meleeDice} Rojo⚔", color = ImperialRed, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Distancia", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                                Text("${stance.rangedDice} Rojo⚔", color = ImperialRed, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Defensa Física", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                                Text("${stance.defenseMeleeDice} Blanco🛡", color = RepublicBlue, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Defensa Distancia", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.6f))
                                Text("${stance.defenseRangedDice} Blanco🛡", color = RepublicBlue, fontWeight = FontWeight.Bold)
                            }
                        }

                        Divider(color = LightCharcoal)

                        // Expertise Tables Detailed View
                        Text("Tabla de Pericia Ofensiva (Ataque)", style = MaterialTheme.typography.labelMedium, color = ImperialRed, fontWeight = FontWeight.Bold)
                        stance.attackExpertise.forEach { level ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("👁 x${level.minExpertise}+", color = ShatterpointAmber, fontWeight = FontWeight.Bold, modifier = Modifier.width(48.dp))
                                Text(level.description, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("Tabla de Pericia Defensiva (Defensa)", style = MaterialTheme.typography.labelMedium, color = RepublicBlue, fontWeight = FontWeight.Bold)
                        stance.defenseExpertise.forEach { level ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("👁 x${level.minExpertise}+", color = ShatterpointAmber, fontWeight = FontWeight.Bold, modifier = Modifier.width(48.dp))
                                Text(level.description, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Abilities section
            Text("Habilidades Especiales", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            character.abilities.forEach { ability ->
                SectionCard(backgroundColor = LightCharcoal) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(ability.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Badge(containerColor = if (ability.cost > 0) AllianceCyan else SeparatistGreen) {
                                    Text(
                                        text = if (ability.cost > 0) "${ability.cost} FP" else "Gratis",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                                Badge(containerColor = LightCharcoal) {
                                    Text(ability.type, color = Color.White, fontSize = 10.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(ability.description, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    } else {
        // Character Directory List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PERSONAJES CAJA BASE & COMPAÑÍA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShatterpointAmber
                )
                Button(
                    onClick = { showCreateForm = true },
                    colors = ButtonDefaults.buttonColors(containerColor = SeparatistGreen),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir", tint = Color.Black)
                    Text("Añadir", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar por nombre, facción o afiliación", color = Color.White.copy(0.6f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = ShatterpointAmber,
                    unfocusedBorderColor = LightCharcoal
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("character_search_input")
            )

            // Filtering Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(null, "Principal", "Secundario", "Apoyo").forEach { type ->
                    val isSelected = selectedTypeFilter == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTypeFilter = type },
                        label = { Text(type ?: "Todos", color = Color.White) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ShatterpointAmber,
                            containerColor = LightCharcoal
                        )
                    )
                }
            }

            // Custom Character Creator inline sheet dialog
            if (showCreateForm) {
                SectionCard(borderStroke = BorderStroke(1.dp, SeparatistGreen)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Crear Personaje Personalizado", style = MaterialTheme.typography.titleSmall, color = SeparatistGreen, fontWeight = FontWeight.Bold)
                        
                        OutlinedTextField(
                            value = newCharName,
                            onValueChange = { newCharName = it },
                            label = { Text("Nombre del Personaje") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newCharTitle,
                            onValueChange = { newCharTitle = it },
                            label = { Text("Título / Cargo") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newCharStamina,
                                onValueChange = { newCharStamina = it },
                                label = { Text("Stamina") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newCharDurability,
                                onValueChange = { newCharDurability = it },
                                label = { Text("Durability") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { showCreateForm = false },
                                colors = ButtonDefaults.buttonColors(containerColor = FailureGrey),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    if (newCharName.isNotBlank()) {
                                        val custom = CharacterEntity(
                                            id = "custom_${newCharName.lowercase().replace(" ", "_")}",
                                            name = newCharName,
                                            title = newCharTitle.ifBlank { "Luchador Personalizado" },
                                            type = newCharType,
                                            faction = newCharFaction,
                                            forcePoints = newCharForce.toIntOrNull() ?: 0,
                                            stamina = newCharStamina.toIntOrNull() ?: 9,
                                            durability = newCharDurability.toIntOrNull() ?: 2,
                                            abilities = listOf(
                                                Ability("Habilidad Táctica", "Activa", 1, "Realiza un movimiento táctico básico o cura 1 herida de sí mismo.")
                                            ),
                                            stances = listOf(
                                                Stance(
                                                    name = "Estilo de Combate Personalizado",
                                                    meleeDice = 6,
                                                    rangedDice = 5,
                                                    defenseMeleeDice = 4,
                                                    defenseRangedDice = 4,
                                                    attackExpertise = listOf(ExpertiseLevel(1, "1+: +1 Impacto", addedStrikes = 1)),
                                                    defenseExpertise = listOf(ExpertiseLevel(1, "1+: +1 Bloqueo", addedBlocks = 1))
                                                )
                                            )
                                        )
                                        viewModel.saveCharacter(custom)
                                        showCreateForm = false
                                        newCharName = ""
                                        newCharTitle = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SeparatistGreen),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Crear", color = Color.Black)
                            }
                        }
                    }
                }
            }

            // Results List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredCharacters) { char ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.activeInspectCharacter.value = char }
                            .testTag("character_card_${char.id}"),
                        colors = CardDefaults.cardColors(containerColor = DeepCharcoal),
                        border = BorderStroke(
                            width = 1.dp,
                            color = when (char.type) {
                                "Principal" -> ShatterpointAmber.copy(alpha = 0.4f)
                                "Secundario" -> AllianceCyan.copy(alpha = 0.4f)
                                else -> JediSilver.copy(alpha = 0.4f)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = char.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Badge(containerColor = when (char.type) {
                                        "Principal" -> ShatterpointAmber
                                        "Secundario" -> AllianceCyan
                                        else -> JediSilver
                                    }) {
                                        Text(char.type, color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = char.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(0.7f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = char.faction,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ShatterpointAmber,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Ver Detalles",
                                tint = Color.White.copy(0.4f)
                            )
                        }
                    }
                }
            }
        }
    }
}




