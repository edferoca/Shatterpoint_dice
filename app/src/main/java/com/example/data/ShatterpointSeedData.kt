package com.example.data

object ShatterpointSeedData {
    val coreBoxCharacters = listOf(
        CharacterEntity(
            id = "anakin_skywalker",
            name = "Anakin Skywalker",
            title = "General de la República",
            type = "Principal",
            faction = "República Galáctica / Jedi",
            forcePoints = 4,
            stamina = 11,
            durability = 2,
            abilities = listOf(
                Ability("La Fuerza es mi aliada", "Activa", 1, "Un personaje de esta unidad puede realizar un movimiento de salto y luego realizar un ataque físico."),
                Ability("Esto se acabó", "Identidad", 0, "Cuando un aliado de la República Galáctica es herido por un ataque enemigo, Anakin gana 1 punto de Fuerza o cura 2 heridas de sí mismo."),
                Ability("Reflejo de la Fuerza", "Reactiva", 1, "Cuando esta unidad es atacada y se saca al menos una Pericia en la tirada de defensa, inflige 2 puntos de daño de vuelta al atacante."),
                Ability("I'm going to end this", "Innata", 0, "Anakin añade 2 dados a sus ataques de combate cuerpo a cuerpo contra personajes Principales enemigos.")
            ),
            stances = listOf(
                Stance(
                    name = "Forma V Djem So",
                    meleeDice = 7,
                    rangedDice = 5,
                    defenseMeleeDice = 5,
                    defenseRangedDice = 4,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Impacto", addedStrikes = 1),
                        ExpertiseLevel(3, "3-4 Pericias: +2 Impactos", addedStrikes = 2),
                        ExpertiseLevel(5, "5+ Pericias: +1 Crítico, +2 Impactos", addedCrits = 1, addedStrikes = 2)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Bloqueo", addedBlocks = 1),
                        ExpertiseLevel(3, "3-4 Pericias: +1 Bloqueo, +1 Esquiva", addedBlocks = 1, addedDodges = 1),
                        ExpertiseLevel(5, "5+ Pericias: +2 Bloqueos, +1 Esquiva", addedBlocks = 2, addedDodges = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "ahsoka_tano",
            name = "Ahsoka Tano",
            title = "Fuerza de la Naturaleza",
            type = "Principal",
            faction = "República Galáctica / Fuerza",
            forcePoints = 4,
            stamina = 9,
            durability = 2,
            abilities = listOf(
                Ability("Acrobacia", "Activa", 1, "Ahsoka realiza un salto corto de forma inmediata."),
                Ability("Hermana pequeña", "Reactiva", 1, "Cuando un aliado de la República Galáctica o Fuerza es atacado, Ahsoka puede avanzar inmediatamente hacia el atacante."),
                Ability("Defensa con dos sables", "Innata", 0, "Gana 1 dado adicional de defensa frente a todos los ataques físicos de cuerpo a cuerpo.")
            ),
            stances = listOf(
                Stance(
                    name = "Forma V Shien Jar'Kai",
                    meleeDice = 7,
                    rangedDice = 0,
                    defenseMeleeDice = 5,
                    defenseRangedDice = 5,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Impacto", addedStrikes = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Crítico, +1 Impacto", addedCrits = 1, addedStrikes = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Bloqueo", addedBlocks = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Bloqueo, +1 Esquiva", addedBlocks = 1, addedDodges = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "capitan_rex",
            name = "Capitán Rex",
            title = "Comandante de la 501",
            type = "Secundario",
            faction = "República Galáctica / Soldado Clon / 501",
            forcePoints = 0,
            stamina = 9,
            durability = 2,
            abilities = listOf(
                Ability("¡Por la República!", "Activa", 1, "Todos los aliados de la 501 o soldados Clon cercanos pueden avanzar un corto trayecto y ganar cobertura."),
                Ability("Instinto de supervivencia", "Innata", 0, "Reduce todo el daño sufrido en 1 punto y se cura de una herida al inicio de su activación."),
                Ability("Hermanos de armas", "Innata", 0, "Mientras esté cerca de otro clon, gana inmunidad al empuje.")
            ),
            stances = listOf(
                Stance(
                    name = "Especialista Táctico",
                    meleeDice = 5,
                    rangedDice = 6,
                    defenseMeleeDice = 4,
                    defenseRangedDice = 4,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Impacto", addedStrikes = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Crítico", addedCrits = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Bloqueo", addedBlocks = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Esquiva", addedDodges = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "soldados_clon_501",
            name = "Soldados Clon de la 501",
            title = "Infantería de Élite",
            type = "Apoyo",
            faction = "República Galáctica / Soldado Clon / 501",
            forcePoints = 0,
            stamina = 7,
            durability = 2,
            abilities = listOf(
                Ability("Maniobra de flanqueo", "Innata", 0, "Ganan 1 dado adicional de ataque si atacan a un enemigo que ya esté trabado en combate cuerpo a cuerpo con otro clon."),
                Ability("Entrenamiento en la 501", "Innata", 0, "Mientras estén a cubierto, ganan +1 dado en defensas físicas.")
            ),
            stances = listOf(
                Stance(
                    name = "Entrenamiento Clon",
                    meleeDice = 4,
                    rangedDice = 5,
                    defenseMeleeDice = 4,
                    defenseRangedDice = 4,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1+ Pericias: +1 Impacto", addedStrikes = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1+ Pericias: +1 Bloqueo", addedBlocks = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "darth_maul",
            name = "Darth Maul",
            title = "Buscador de Venganza",
            type = "Principal",
            faction = "Colectivo de Sombra / Sith",
            forcePoints = 3,
            stamina = 11,
            durability = 2,
            abilities = listOf(
                Ability("Salto de Fuerza", "Activa", 1, "Permite a Maul saltar una distancia vertical considerable."),
                Ability("La ira me sostiene", "Identidad", 0, "Darth Maul puede usar sus heridas acumuladas para añadir dados adicionales a sus ataques de cuerpo a cuerpo (1 dado extra por cada 2 heridas)."),
                Ability("Odio implacable", "Reactiva", 1, "Cuando reciba daño, puede avanzar de inmediato una distancia corta hacia el atacante.")
            ),
            stances = listOf(
                Stance(
                    name = "Estilo de Combate Juyo Brutal",
                    meleeDice = 8,
                    rangedDice = 0,
                    defenseMeleeDice = 4,
                    defenseRangedDice = 4,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Impacto", addedStrikes = 1),
                        ExpertiseLevel(3, "3-4 Pericias: +2 Impactos", addedStrikes = 2),
                        ExpertiseLevel(5, "5+ Pericias: +2 Críticos", addedCrits = 2)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Bloqueo", addedBlocks = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Esquiva", addedDodges = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "gar_saxon",
            name = "Gar Saxon",
            title = "Comandante Mandaloriano",
            type = "Secundario",
            faction = "Colectivo de Sombra / Mandaloriano",
            forcePoints = 0,
            stamina = 9,
            durability = 2,
            abilities = listOf(
                Ability("Disparo de cohete", "Activa", 1, "Realiza un ataque de cohetes a larga distancia que ignora coberturas e inflige tensión."),
                Ability("Liderazgo despiadado", "Innata", 0, "Cuando un enemigo sufra daño y esté cerca, Saxon puede reposicionarse o realizar un ataque gratis."),
                Ability("Ataque con propulsor", "Activa", 1, "Realiza un salto de movimiento y gana dados adicionales en el próximo ataque físico.")
            ),
            stances = listOf(
                Stance(
                    name = "Tácticas de Supercomando",
                    meleeDice = 6,
                    rangedDice = 6,
                    defenseMeleeDice = 5,
                    defenseRangedDice = 5,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Impacto", addedStrikes = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Crítico", addedCrits = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Bloqueo", addedBlocks = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Esquiva", addedDodges = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "supercomandos_mandalorianos",
            name = "Supercomandos Mandalorianos",
            title = "Colectivo de Sombra",
            type = "Apoyo",
            faction = "Colectivo de Sombra / Mandaloriano",
            forcePoints = 0,
            stamina = 7,
            durability = 2,
            abilities = listOf(
                Ability("Propulsores", "Activa", 1, "Realizan un salto y ganan posición de ventaja elevada."),
                Ability("Carga coordinada", "Innata", 0, "Cuando avancen junto a Gar Saxon, se mueven gratis adicionalmente.")
            ),
            stances = listOf(
                Stance(
                    name = "Asalto Mandaloriano",
                    meleeDice = 5,
                    rangedDice = 5,
                    defenseMeleeDice = 4,
                    defenseRangedDice = 4,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1+ Pericias: +1 Impacto", addedStrikes = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1+ Pericias: +1 Bloqueo", addedBlocks = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "bo_katan_kryze",
            name = "Bo-Katan Kryze",
            title = "Líder del Clan Kryze",
            type = "Secundario",
            faction = "Mandaloriano / Fuerza",
            forcePoints = 0,
            stamina = 9,
            durability = 2,
            abilities = listOf(
                Ability("¡Mandalorianos, uníos!", "Activa", 1, "Permite a un mandaloriano aliado avanzar un trayecto corto y ganar cobertura de inmediato."),
                Ability("Líder por derecho de nacimiento", "Innata", 0, "Todos los aliados mandalorianos cercanos añaden +1 dado a sus defensas."),
                Ability("Por Mandalore", "Reactiva", 1, "Cuando un enemigo intente puntuar un objetivo cerca, realiza un ataque de cuerpo a cuerpo gratis.")
            ),
            stances = listOf(
                Stance(
                    name = "Firmeza de Mandalore",
                    meleeDice = 6,
                    rangedDice = 6,
                    defenseMeleeDice = 5,
                    defenseRangedDice = 5,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Impacto", addedStrikes = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Crítico", addedCrits = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Bloqueo", addedBlocks = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Esquiva, +1 Bloqueo", addedDodges = 1, addedBlocks = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "clan_kryze_mandalorians",
            name = "Clan Kryze Mandalorians",
            title = "Mandalorianos Leales",
            type = "Apoyo",
            faction = "Mandaloriano / Fuerza",
            forcePoints = 0,
            stamina = 7,
            durability = 2,
            abilities = listOf(
                Ability("Fuego de cobertura", "Reactiva", 1, "Cuando un aliado activa un ataque a distancia, inflige la condición de Tensión al enemigo atacado."),
                Ability("Ataque rápido", "Activa", 1, "Salto táctico corto y avance de ataque coordinado.")
            ),
            stances = listOf(
                Stance(
                    name = "Tácticas de Clan",
                    meleeDice = 5,
                    rangedDice = 5,
                    defenseMeleeDice = 4,
                    defenseRangedDice = 4,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1+ Pericias: +1 Impacto", addedStrikes = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1+ Pericias: +1 Bloqueo", addedBlocks = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "asajj_ventress",
            name = "Asajj Ventress",
            title = "Asesina Sith",
            type = "Principal",
            faction = "Separatista / Sith",
            forcePoints = 4,
            stamina = 10,
            durability = 2,
            abilities = listOf(
                Ability("Fuerza emboscadora", "Activa", 1, "Realiza un salto de Fuerza y entra en posición ofensiva."),
                Ability("La ira de las Hermanas", "Identidad", 0, "Gana +1 dado de ataque físico por cada aliado Separatista derrotado o herido."),
                Ability("Manipulación mental", "Reactiva", 1, "Obliga a un enemigo cercano a retroceder o alejarse de un objetivo clave.")
            ),
            stances = listOf(
                Stance(
                    name = "Forma IV Ataru Agresivo",
                    meleeDice = 7,
                    rangedDice = 0,
                    defenseMeleeDice = 5,
                    defenseRangedDice = 5,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Impacto", addedStrikes = 1),
                        ExpertiseLevel(3, "3-4 Pericias: +2 Impactos", addedStrikes = 2),
                        ExpertiseLevel(5, "5+ Pericias: +1 Crítico, +1 Impacto", addedCrits = 1, addedStrikes = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Bloqueo", addedBlocks = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Esquiva", addedDodges = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "kalani_supertactico",
            name = "Kalani",
            title = "Supertáctico",
            type = "Secundario",
            faction = "Separatista / Droide",
            forcePoints = 0,
            stamina = 9,
            durability = 2,
            abilities = listOf(
                Ability("Cálculo analítico", "Activa", 1, "Asigna un marcador que reduce los dados de defensa del objetivo en 1."),
                Ability("Estrategia robótica", "Innata", 0, "Añade +1 dado a todos los ataques a distancia de Droides aliados cercanos."),
                Ability("Redirección defensiva", "Reactiva", 1, "Cuando un droide aliado sea atacado, puede traspasar parte del daño a droides de apoyo cercanos.")
            ),
            stances = listOf(
                Stance(
                    name = "Protocolo de Combate Computado",
                    meleeDice = 5,
                    rangedDice = 6,
                    defenseMeleeDice = 4,
                    defenseRangedDice = 5,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Impacto", addedStrikes = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Crítico", addedCrits = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1-2 Pericias: +1 Bloqueo", addedBlocks = 1),
                        ExpertiseLevel(3, "3+ Pericias: +1 Bloqueo", addedBlocks = 1)
                    )
                )
            )
        ),
        CharacterEntity(
            id = "droides_combate_b1",
            name = "Droides de Combate B1",
            title = "Enjambre de Chatarra",
            type = "Apoyo",
            faction = "Separatista / Droide",
            forcePoints = 0,
            stamina = 7,
            durability = 2,
            abilities = listOf(
                Ability("Número abrumador", "Innata", 0, "Por cada otra unidad de droides B1 aliada trabada con el objetivo, gana +1 dado a sus ataques."),
                Ability("Programación simple", "Innata", 0, "No se les puede imponer marcadores de tensión, pero tampoco pueden beneficiarse de coberturas físicas.")
            ),
            stances = listOf(
                Stance(
                    name = "Protocolo de Enjambre",
                    meleeDice = 4,
                    rangedDice = 4,
                    defenseMeleeDice = 3,
                    defenseRangedDice = 3,
                    attackExpertise = listOf(
                        ExpertiseLevel(1, "1+ Pericias: +1 Impacto", addedStrikes = 1)
                    ),
                    defenseExpertise = listOf(
                        ExpertiseLevel(1, "1+ Pericias: +1 Bloqueo", addedBlocks = 1)
                    )
                )
            )
        )
    )
}
