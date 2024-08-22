package com.pools.store.base

interface BaseUseCase<in Parameter, out Result> {
   suspend fun execute (params: Parameter): Result
}
