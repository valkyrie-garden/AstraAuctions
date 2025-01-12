package com.astrainteractive.astramarket.api

import com.astrainteractive.astramarket.modules.Modules
import ru.astrainteractive.astralibs.di.getValue

private val translation by Modules.translation
enum class AuctionSort(val desc: String) {
    MATERIAL_DESC(translation.sortMaterialDesc),
    MATERIAL_ASC(translation.sortMaterialAsc),
    DATE_DESC(translation.sortDateDesc),
    DATE_ASC(translation.sortDateAsc),
    NAME_DESC(translation.sortNameDesc),
    NAME_ASC(translation.sortNameAsc),
    PRICE_DESC(translation.sortPriceDesc),
    PRICE_ASC(translation.sortPriceAsc),
    PLAYER_ASC(translation.sortPlayerAsc),
    PLAYER_DESC(translation.sortPlayerDesc);
}

