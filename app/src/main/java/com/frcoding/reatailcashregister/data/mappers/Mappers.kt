package com.frcoding.reatailcashregister.data.mappers

import com.frcoding.reatailcashregister.data.dto.InvoiceDto
import com.frcoding.reatailcashregister.data.dto.ItemDto
import com.frcoding.reatailcashregister.data.dto.UserDto
import com.frcoding.reatailcashregister.models.Invoice
import com.frcoding.reatailcashregister.models.Item
import com.frcoding.reatailcashregister.models.User

fun ItemDto.toItem(): Item = Item(
    id = this.id,
    name = this.name,
    quantity = this.quantity,
    price = this.price
)

fun Item.toItemDto(): ItemDto = ItemDto(
    id = this.id,
    name = this.name,
    quantity = this.quantity,
    price = this.price
)

fun InvoiceDto.toInvoice(): Invoice = Invoice(
    id = this.id,
    userId = this.userId,
    paymentMethod = this.paymentMethod,
    totalPrice = this.totalPrice
)

fun Invoice.toInvoiceDto(): InvoiceDto = InvoiceDto(
    id = this.id,
    userId = this.userId,
    paymentMethod = this.paymentMethod,
    totalPrice = this.totalPrice
)

fun UserDto.toUser(): User = User(
    id = this.id,
    username = this.username,
    password = this.password
)

fun User.toUserDto(): UserDto = UserDto(
    id = this.id,
    username = this.username,
    password = this.password
)