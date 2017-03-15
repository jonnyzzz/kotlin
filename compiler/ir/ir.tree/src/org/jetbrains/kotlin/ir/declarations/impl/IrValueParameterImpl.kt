/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.ir.declarations.impl

import org.jetbrains.kotlin.descriptors.ParameterDescriptor
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.expressions.impl.IrExpressionBodyImpl
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor

class IrValueParameterImpl(
        startOffset: Int,
        endOffset: Int,
        origin: IrDeclarationOrigin,
        override val descriptor: ParameterDescriptor
) : IrDeclarationBase(startOffset, endOffset, origin), IrValueParameter {
    constructor(
            startOffset: Int,
            endOffset: Int,
            origin: IrDeclarationOrigin,
            descriptor: ParameterDescriptor,
            defaultValue: IrExpressionBody?
    ) : this(startOffset, endOffset, origin, descriptor) {
        this.defaultValue = defaultValue
    }

    constructor(
            startOffset: Int,
            endOffset: Int,
            origin: IrDeclarationOrigin,
            descriptor: ParameterDescriptor,
            defaultValue: IrExpression
    ) : this(startOffset, endOffset, origin, descriptor, IrExpressionBodyImpl(defaultValue))

    override var defaultValue: IrExpressionBody? = null

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R =
            visitor.visitValueParameter(this, data)

    override fun <D> transform(transformer: IrElementTransformer<D>, data: D): IrValueParameter =
            transformer.visitValueParameter(this, data) as IrValueParameter

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        defaultValue?.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        defaultValue = defaultValue?.transform(transformer, data)
    }
}