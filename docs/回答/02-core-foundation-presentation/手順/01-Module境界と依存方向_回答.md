# 手順01回答: Module境界と依存方向

## 確認対象

- `BaseArchitecture/settings.gradle.kts`
- `BaseArchitecture/core/foundation/build.gradle.kts`
- `BaseArchitecture/core/presentation/build.gradle.kts`
- `BaseArchitecture/app/build.gradle.kts`

## 依存方向

```text
core:foundation
        ↑
core:presentation
        ↑
       app
```

`core:foundation`は`AppResult`、`AppError`、ValidationなどのAndroid非依存契約を保持する。`core:presentation`はFoundationの契約を利用し、`UiState`、`UiEvent`、`UiMessage`、`UiEffect`、`Reducer`、`BaseViewModel`を提供する。

FoundationがPresentationやAppへ依存しないため、UseCaseやRepositoryがUIフレームワークへ引きずられず、Coreを別Featureや別アプリへ再利用できる。

## Moduleを分ける理由

PresentationまでFoundationへ置くと、ResultやErrorの契約を利用するだけでStateFlowやViewModelなどのUI基盤にも依存する。責務を分けることで、Domain寄りの処理はFoundationだけを参照し、画面を持つ処理だけがPresentationを参照できる。

HiltやComposeなどのAndroid依存処理は、Foundationの契約へ入れない。依存性の注入や画面接続は上位Moduleで行う。

## 回答

Feature固有のState、Event、UseCase、Repository、業務ルールはCoreへ追加しない。CoreはFeatureが同じ契約で実装するための境界を提供し、Featureの具体的な処理は`app`側へ残す。

