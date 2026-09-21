### 🌐 Language / 言語
[English](./README.md) | 日本語

---

# Sneak-Jump

## プラグイン説明
Minecraft (Paper/Spigot) 向けの特殊効果付きブーツ追加プラグインです。  
二段ジャンプやスニーク蓄積からの爆発的ジャンプなど、プレイスタイルに合わせた多様な空中アクションを提供します。

---

### 🌟 追加されるブーツ

#### 1. Double Jump Boots (二段ジャンプブーツ)
空中でもう一度ジャンプを行うことができるベーシックなブーツです。移動や高所への登頂がスムーズになります。

* **耐久消費:** `0` (無制限)

| プレビュー | クラフトレシピ |
| :---: | :---: |
| <img src="imgs/double_jump.gif" width="350"> | <img src="imgs/double_jump_recipe.png" width="280"> |

---

#### 2. Sneak Jump Boots (スニークジャンプブーツ)
スニークを入力することで少し高く跳び上がることができるブーツです。

* **耐久消費:** `1`

| プレビュー | クラフトレシピ |
| :---: | :---: |
| <img src="imgs/sneak_jump.gif" width="350"> | <img src="imgs/sneak_jump_recipe.png" width="280"> |

---

#### 3. TNT Jump Boots (TNTジャンプブーツ)
スニークを連続入力してTNTエネルギーを蓄積し、ジャンプで一気に真上へドカンと打ち上がる必殺技ブーツです。

* **操作方法:** スニーク連打（最大5チャージ） ➔ ジャンプで発射
* **耐久消費:** `5` (革のブーツ1足につき13回使用可能)

| プレビュー | クラフトレシピ |
| :---: | :---: |
| <img src="imgs/tnt_jump.gif" width="350"> | <img src="imgs/tnt_jump_recipe.png" width="280"> |

---

## プラグインダウンロード
[ダウンロードリンク](https://github.com/ringoame196-s-mcPlugin/Sneak-Jump/releases/latest)

## コマンド
| コマンド名 | 説明 | 権限 |
| --- | --- | --- |
| `/sneak-jump give <id>` | 指定したIDの特殊ブーツを入手します (エイリアス: `/sjump`) | `sneak_jump.admin` |

## 使い方
1. 各ブーツに対応したクラフトレシピ（または管理者コマンド `/sneak-jump give <id>`）で特殊ブーツを入手します。
2. ブーツを装備（足スロットに装着）します。
3. ブーツごとの操作（空中ジャンプ、スニーク、スニーク連打＋ジャンプ）を行うことで特殊アクションが発動します。
4. 各アクションごとに設定された耐久値（0 / 1 / 5）を消費します。

* **注意点:** 傷のついた（耐久値が減っている）革のブーツはクラフト素材として使用できません。また、ブーツを装備してのジャンプアクションによる落下ダメージは保護されます。

## configファイル
| key名 | 説明 | デフォルト値 |
| --- | --- | --- |
| `enable-crafting` | 特殊ブーツのクラフトレシピを有効化するかどうか | `true` |
| `names.double_jump_boots` | 二段ジャンプブーツの表示名 | `"&b&lDouble Jump Boots"` |
| `names.sneak_jump_boots` | スニークジャンプブーツの表示名 | `"&a&lSneak Jump Boots"` |
| `names.tnt_jump_boots` | TNTジャンプブーツの表示名 | `"&c&lTNT Jump Boots"` |

## 開発環境
- Minecraft Version : 1.20.1
- Kotlin Version : 1.8.0

## プロジェクト情報
- プロジェクトパス : ringoame196-s-mcPlugin/Sneak-Jump.git
- 開発者名 : ringoame196-s-mcPlugin
- 開発開始日 : 2026-08-22